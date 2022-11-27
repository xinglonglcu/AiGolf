package com.xlong.aigolf.viewmodel

import com.xlong.data.model.AccountModel
import com.xlong.data.model.VideoModel
import com.xlong.data.net.ApiClient
import com.xlong.data.net.BaseModel
import com.xlong.mvi.ResponseStateReducer
import com.xlong.mvi.action.RxActionDeDuper
import com.xlong.mvi.action.rxAction
import com.xlong.mvi.data.MutableObservableList
import com.xlong.mvi.data.ObservableList
import com.xlong.mvi.vm.RxViewModel

/**
 *
 * Create by xlong 2022/11/25
 */
class MainViewModel : RxViewModel() {
    private val TAG = "HomeViewModel"
    private val mDeduper = RxActionDeDuper()
    private val videoReducer = ResponseStateReducer<Any, List<VideoModel>>()
    private var videoPage = 1
    private val _videoList = MutableObservableList<VideoModel>()
    val videoList: ObservableList<VideoModel> = _videoList

    private val collectReducer = ResponseStateReducer<Any, List<VideoModel>>()
    private var collectPage = 1
    private val _collectList = MutableObservableList<VideoModel>()
    val collectList: ObservableList<VideoModel> = _collectList

    private val poseReducer = ResponseStateReducer<Any, List<VideoModel>>()
    private var posePage = 1
    private val _poseList = MutableObservableList<VideoModel>()
    val poseList: ObservableList<VideoModel> = _poseList

    init {
        videoReducer.observable.filter { it.isFail || it.isSuccess }.subscribe {
            if (it.isSuccess && !it.data.isNullOrEmpty()) {
                if (videoPage == 1) {
                    _videoList.clear()
                }
                _videoList.addAll(it.data!!)
                videoPage++
            }
        }
    }

    fun getVideoList(refresh: Boolean) {
        if (refresh) {
            videoPage = 1
        }
        rxAction<BaseModel<List<VideoModel>>> {
            observable = ApiClient.getInstance().basicService.getVideoList(videoPage)
            token = "getVideoList"
            dispatcher = videoReducer
            deDuper = mDeduper
        }.execute()

        addTestVideo(_videoList)// TODO: 2022/11/25
    }

    /**
     * 我的收藏
     */
    fun getMyCollection(refresh: Boolean) {
        if (refresh) {
            collectPage = 1
        }
        rxAction<BaseModel<List<VideoModel>>> {
            observable = ApiClient.getInstance().basicService.getMyCollectList(collectPage)
            token = "getMyCollection"
            dispatcher = collectReducer
            deDuper = mDeduper
        }.execute()
        addTestVideo(_collectList)// TODO: 2022/11/25
    }

    /**
     * 姿势库
     */
    fun getPoseLibList(refresh: Boolean, map: Map<String, String>) {
        if (refresh) {
            posePage = 1
        }
        rxAction<BaseModel<List<VideoModel>>> {
            observable = ApiClient.getInstance().basicService.getPoseLibList(posePage)
            token = "getPoseLib"
            dispatcher = poseReducer
            deDuper = mDeduper
        }.execute()
        addTestVideo(_poseList)// TODO: 2022/11/25
    }

    private fun addTestVideo(observableList: MutableObservableList<VideoModel>) {

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
        val model8 = VideoModel("8", "8", "8", "教练教练", avatar, cover, "标题标题标题", playUrl, 2456, 66, 0, 555555, 0, 66, 100, 1234, 0)
        val model9 = VideoModel("9", "9", "9", "教练教练教练教练", avatar, cover, "标题标题标题", playUrl, 456, 66, 0, 4444, 0, 66, 100, 1234, 0)
        val model10 = VideoModel("10", "10", "10", "教练教练教练教练", avatar, cover, "标题标题标题标题", playUrl, 3456, 66, 0, 22222, 0, 66, 100, 1234, 0)
        val model11 = VideoModel("11", "11", "11", "教练教练教练教练教练教练", avatar, cover, "标题标题标题标题", playUrl, 3456, 66, 0, 33333333, 0, 66, 100, 1234, 0)
        val model12 = VideoModel("12", "12", "12", "教练教练教练教练教练教练教练", avatar, cover, "标题标题标题标题标题标题标题", playUrl, 123, 333333333, 0, 66, 100, 1234, 0)

        val list = mutableListOf<VideoModel>()
        list.add(model1)
        list.add(model2)
        list.add(model3)
        list.add(model4)
        list.add(model5)
        list.add(model6)
        list.add(model7)
        list.add(model8)
        list.add(model9)
//        list.add(model10)
//        list.add(model11)
//        list.add(model12)
        observableList.addAll(list)
    }
}