package com.xlong.aigolf.viewmodel

import com.xlong.data.model.MyInfoModel
import com.xlong.data.model.MyVideoModel
import com.xlong.data.model.MyVideoType
import com.xlong.data.model.VideoModel
import com.xlong.data.net.ApiClient
import com.xlong.data.net.BaseModel
import com.xlong.mvi.ResponseStateReducer
import com.xlong.mvi.action.RxActionDeDuper
import com.xlong.mvi.action.rxAction
import com.xlong.mvi.data.MutableObservableList
import com.xlong.mvi.vm.RxViewModel
import io.reactivex.subjects.PublishSubject

class HomeViewModel : RxViewModel() {
    private val TAG = "HomeViewModel"
    private val mDeduper = RxActionDeDuper()
    private val myInfoReducer = ResponseStateReducer<Int, MyInfoModel>()
    private val myVideoReducer = ResponseStateReducer<Int, List<MyVideoModel>>()
    private val _myVideos = MutableObservableList<MyVideoModel>()
    private val _myAiAnalysisVideos = MutableObservableList<MyVideoModel>()
    private val _myAiCompareVideos = MutableObservableList<MyVideoModel>()
    private val _myAiReportVideos = MutableObservableList<MyVideoModel>()

    private var currentVideoType = MyVideoType.MYVIDEO.type//当前video类型
    val myVideos = MutableObservableList<MyVideoModel>()
    val myInfoSubject = PublishSubject.create<MyInfoModel>()

    init {
        myVideoReducer.observable.filter { it.isFail || it.isSuccess }.subscribe {
            if (it.isSuccess || it.isFail) { // TODO: 2022/11/28
//                it.data?.let { list ->

                val list = addTestData(currentVideoType)

                when (it.metadata) {
                    MyVideoType.MYVIDEO.type -> {
                        _myVideos.addAll(list)
                    }
                    MyVideoType.AI_ANALYSIS.type -> {
                        _myAiAnalysisVideos.addAll(list)
                    }
                    MyVideoType.AI_COMPARE.type -> {
                        _myAiCompareVideos.addAll(list)
                    }
                    MyVideoType.AI_REPORT.type -> {
                        _myAiReportVideos.addAll(list)
                    }
                    else -> {

                    }
                }
                when (currentVideoType) {
                    MyVideoType.MYVIDEO.type -> {
                        myVideos.reset(_myVideos)
                    }
                    MyVideoType.AI_ANALYSIS.type -> {
                        myVideos.reset(_myAiAnalysisVideos)
                    }
                    MyVideoType.AI_COMPARE.type -> {
                        myVideos.reset(_myAiCompareVideos)
                    }
                    MyVideoType.AI_REPORT.type -> {
                        myVideos.reset(_myAiReportVideos)
                    }
                    else -> {

                    }
                }
//                }
            }
        }

        myInfoReducer.observable.filter { it.isSuccess || it.isFail }.subscribe {
            val myInfoModel = MyInfoModel("", 12, 34, 45, 67, 89, 34, 23, 45)
            if (myInfoModel != null) {
                myInfoSubject.onNext(myInfoModel)
            }
        }
    }

    //获取我的视频 type: 1-视频，2-AI分析，3-AI对比，4-AI报告
    fun getMyVideos(type: Int) {
        currentVideoType = type
        rxAction<BaseModel<List<MyVideoModel>>> {
            observable = ApiClient.getInstance().basicService.getMyVideos(type)
            token = "getMyVideo_$type"
            dispatcher = myVideoReducer
            deDuper = mDeduper
            metadata = type
        }.execute()
    }

    fun getMyInfo() {
        rxAction<BaseModel<MyInfoModel>> {
            observable = ApiClient.getInstance().basicService.getMyInfo()
            token = "getMyInfo"
            dispatcher = myInfoReducer
            deDuper = mDeduper
        }.execute()
    }

    private fun addTestData(type: Int): List<MyVideoModel> {

        val avatar = "https://aimg.tangdou.com/public/avatar/2019/0823/p9739530.jpg!small"
        val cover = "https://aimg.tangdou.com/public/video/2019/0820/3A0A209726698B8207C13FC78E995712-1080X1920.jpg!m480"
        val playUrl = "https://acc.tangdou.com/201908/C6DBF431FBA280CFA8EA7915AFC4CC30-20.mp4?sign=5344c4f36d2b943d8cd378d4a34f5e06&stTime=1669371819"

        val model1 = VideoModel("1", "1", "1", "教练", avatar, cover, "标题标题标题标题标题标题", playUrl, 2345, 66, 1, 88, 0, 66, 100, 1234, 0)
        val model2 = VideoModel("2", "2", "2", "教练", avatar, cover, "标题标题标题标题标题", playUrl, 234, 66, 1, 888, 0, 66, 100, 1234, 0)
        val model3 = VideoModel("3", "3", "3", "教练", avatar, cover, "标题标题标题标题标题", playUrl, 23, 66, 1, 88888, 0, 66, 100, 1234, 0)
        val model4 = VideoModel("4", "4", "4", "教练", avatar, cover, "标题标题标题标题", playUrl, 2345, 66, 0, 888888, 0, 66, 100, 1234, 0)
        val model5 = VideoModel("5", "5", "5", "教练", avatar, cover, "标题标题标题标题标题", playUrl, 2345, 66, 0, 5555, 0, 66, 100, 1234, 0)
        val model6 = VideoModel("6", "6", "6", "教练", avatar, cover, "标题标题标题标题标题", playUrl, 2345, 66, 0, 555555, 0, 66, 100, 1234, 0)
        val model7 = VideoModel("7", "7", "7", "教练", avatar, cover, "标题标题标题标题标题标题", playUrl, 2356, 66, 0, 555555, 0, 66, 100, 1234, 0)

        val myModel1 = MyVideoModel("1", type, "2022年11月29日", listOf(model1))
        val myModel2 = MyVideoModel("2", type, "2022年11月29日", listOf(model1, model2))
        val myModel3 = MyVideoModel("4", type, "2022年11月29日", listOf(model1, model2, model3))
        val myModel4 = MyVideoModel("3", type, "2022年11月29日", listOf(model1, model2, model3, model4))
        val myModel5 = MyVideoModel("5", type, "2022年11月29日", listOf(model1, model2, model3, model4, model5))
        val myModel6 = MyVideoModel("6", type, "2022年11月29日", listOf(model1, model2, model3, model4, model5, model6))
        val myModel7 = MyVideoModel("6", type, "2022年11月29日", listOf(model1, model2, model3, model4, model5, model6, model7))

        val list = mutableListOf<MyVideoModel>()
        list.add(myModel1)
        list.add(myModel2)
        list.add(myModel3)
        list.add(myModel4)
        list.add(myModel5)
        list.add(myModel6)
        list.add(myModel7)
        return list
    }

}