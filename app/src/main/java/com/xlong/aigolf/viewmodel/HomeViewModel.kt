package com.xlong.aigolf.viewmodel

import android.util.Log
import com.xlong.data.model.AccountModel
import com.xlong.data.model.LutFilterModel
import com.xlong.data.net.ApiClient
import com.xlong.data.net.BaseModel
import com.xlong.mvi.ResponseStateReducer
import com.xlong.mvi.action.RxActionDeDuper
import com.xlong.mvi.action.rxAction
import com.xlong.mvi.getCode
import com.xlong.mvi.getMessage
import com.xlong.mvi.vm.RxViewModel
import io.reactivex.subjects.PublishSubject

class HomeViewModel : RxViewModel() {
    private val TAG = "HomeViewModel"
    private val mDeduper = RxActionDeDuper()
    private val loginReducer = ResponseStateReducer<Any, AccountModel>()
    private val testReducer = ResponseStateReducer<Any, List<LutFilterModel>>()
    val accountSubject = PublishSubject.create<AccountModel>()

    init {
        loginReducer.observable.subscribe {
            if (it.isSuccess) {
                Log.d(TAG, "loginReducer : isSuccess -- ${it.data}")
                accountSubject.onNext(it.data!!)
            } else if (it.isFail) {
                Log.w(TAG, "loginReducer : isFail - ${it.getCode()} : ${it.getMessage()}")
            } else {
                Log.d(TAG, "loginReducer : isLoading")
            }
        }

        testReducer.observable.subscribe {
            if (it.isSuccess) {
                Log.d(TAG, "testReducer : isSuccess -- ${it.data}")
            } else if (it.isFail) {
                Log.w(TAG, "testReducer : isFail - ${it.getCode()} : ${it.getMessage()}")
            } else {
                Log.d(TAG, "testReducer : isLoading")
            }
        }
    }

    fun login(mobil: String, pwd: String) {
        rxAction<BaseModel<AccountModel>> {
            observable = ApiClient.getInstance().basicService.login(mobil, pwd)
            token = "login"
            dispatcher = loginReducer
            deDuper = mDeduper
        }.execute()
    }

    fun test() {
        rxAction<BaseModel<List<LutFilterModel>>> {
            observable = ApiClient.getInstance().basicService.getRecordFilters()
            token = "login"
            dispatcher = testReducer
            deDuper = mDeduper
        }.execute()
    }

}