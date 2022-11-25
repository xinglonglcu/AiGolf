package com.xlong.aigolf.viewmodel

import com.xlong.data.model.AccountModel
import com.xlong.data.model.VideoModel
import com.xlong.data.net.ApiClient
import com.xlong.data.net.BaseModel
import com.xlong.mvi.ResponseStateReducer
import com.xlong.mvi.action.RxActionDeDuper
import com.xlong.mvi.action.rxAction
import com.xlong.mvi.vm.RxViewModel

/**
 *
 * Create by xlong 2022/11/25
 */
class MainViewModel : RxViewModel() {
    private val TAG = "HomeViewModel"
    private val mDeduper = RxActionDeDuper()
    private val videoReducer = ResponseStateReducer<Any, List<VideoModel>>()

    init {

    }
    fun getVideoList(refresh:Boolean) {

//        rxAction<BaseModel<AccountModel>> {
//            observable = ApiClient.getInstance().basicService.login(mobil, pwd)
//            token = "login"
//            dispatcher = loginReducer
//            deDuper = mDeduper
//        }.execute()
    }
}