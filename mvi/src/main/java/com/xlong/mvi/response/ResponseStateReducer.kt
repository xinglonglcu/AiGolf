package com.xlong.mvi

import android.content.Intent
import com.xlong.data.net.BaseModel
import com.xlong.mvi.action.*
import com.xlong.mvi.response.PagingMetadata
import com.xlong.mvi.response.StateData
import com.xlong.mvi.store.ObservableReducer
import java.io.EOFException

/**
 * 专门用于处理HttpResponse的ObservableReducer，会把BaseModel包裹的数据转化为我们需要的有状态的结果数据类型
 *
 * M -> Metadata type
 * R -> Result type
 */
class ResponseStateReducer<M, R>(isBehavior: Boolean = false) : ObservableReducer<BaseModel<R>, StateData<M, R>>(isBehavior, {
    when (val async = it.actionAsync as ActionAsync<M>) {
        is Uninitialized, is Loading<*> -> {
            StateData<M, R>(null, async)
        }
        is Success<*, *> -> {
            val success = async as Success<M, BaseModel<R>?>
            val data = success.data
            if (data?.pagesize != null && (async.metadata as? PagingMetadata<*>)?.size == -1) {
                (async.metadata as PagingMetadata<*>).size = data.pagesize
            }
            when {
                data?.code != 0 -> StateData<M, R>(data = null, actionAsync = Fail(async.metadata, CodeErrorException(data?.code ?: -1, data?.msg ?: "操作失败")))
                else -> StateData(success.data?.datas, success, data.endid, data.msg, data.param, data.pagesize)
            }
        }
        is Fail -> {
            StateData<M, R>(null, async)
        }
    }
})

fun <M, R> reduceActionNonNull(it: RxAction<*, BaseModel<R>>): StateData<M, R> {
    return when (val async = it.actionAsync as ActionAsync<M>) {
        is Uninitialized, is Loading<*> -> {
            StateData<M, R>(null, async)
        }
        is Success<*, *> -> {
            val success = async as Success<M, BaseModel<R>?>
            val data = success.data
            if (data?.pagesize != null && (async.metadata as? PagingMetadata<*>)?.size == -1) {
                (async.metadata as PagingMetadata<*>).size = data.pagesize
            }

            when {
                data?.code != 0 -> StateData<M, R>(null, Fail(async.metadata, CodeErrorException(data?.code ?: -1, data?.msg ?: "操作失败")))
                success.data?.datas == null -> StateData<M, R>(null, Fail(async.metadata, DataNotFoundException("没有找到数据")))
                else -> StateData(success.data?.datas, success, data.endid, data.msg, data.param, data.pagesize)
            }
        }
        is Fail -> {
            StateData<M, R>(null, async)
        }
    }
}

class ResponseStateNonNullReducer<M, R>(isBehavior: Boolean = false) : ObservableReducer<BaseModel<R>, StateData<M, R>>(isBehavior, ::reduceActionNonNull)

fun <M, R> StateData<M, R>.getMessage(): String {
    return when (actionAsync) {
        is Uninitialized, is Loading -> "加载中"
        is Success<*, *> -> this.msg ?: "操作失败"
        is Fail<M> -> {
            return when (val exception = actionAsync.e) {
                is CodeErrorException, is LoginExpiredException, is DataNotFoundException -> exception.message ?: "操作失败"
                is EOFException -> "操作失败"
                else -> "网络连接失败，请检查网络设置,${exception.message}"
            }
        }
    }
}

fun <M, R> StateData<M, R>.getCode(): Int {
    return when (actionAsync) {
        is Uninitialized, is Loading -> 0
        is Success<*, *> -> 0
        is Fail<M> -> {
            return when (val exception = (actionAsync as Fail).e) {
                is CodeErrorException -> exception.code
                is DataNotFoundException -> -1
                else -> UNKNOWN_ERROR_CODE
            }

        }
    }
}

fun <M, R> StateData<M, R>.getParam(): Map<*, *>? {
    return when (actionAsync) {
        is Success<*, *> -> this.ext as? Map<*, *>
        else -> null
    }
}

const val UNKNOWN_ERROR_CODE = -0x12345

class CodeErrorException(val code: Int, val msg: String) : Exception(msg)

class LoginExpiredException(msg: String) : Exception(msg)

class DataNotFoundException(msg: String) : Exception(msg)