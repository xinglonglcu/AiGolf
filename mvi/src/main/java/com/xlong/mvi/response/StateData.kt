package com.xlong.mvi.response

import com.xlong.mvi.action.ActionAsync
import com.xlong.mvi.action.Fail
import com.xlong.mvi.action.Loading
import com.xlong.mvi.action.Success

/**
 * State is a data wrapper for most reducers
 */
data class StateData<M, R>(
    val data: R?,
    val actionAsync: ActionAsync<M>,
    val endId: String? = "",
    val msg: String? = "加载成功",
    val ext: Any? = null,
    val pageSize: Int = 0
) {
    val metadata: M?
        get() = actionAsync.metadata
    val isLoading = actionAsync is Loading<M>
    val isSuccess = actionAsync is Success<M, *>
    val isFail = actionAsync is Fail<M>
}
