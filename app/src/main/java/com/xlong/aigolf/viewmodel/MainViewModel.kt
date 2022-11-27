package com.xlong.aigolf.viewmodel

import com.xlong.data.model.*
import com.xlong.data.net.ApiClient
import com.xlong.data.net.BaseModel
import com.xlong.mvi.ResponseStateReducer
import com.xlong.mvi.action.RxActionDeDuper
import com.xlong.mvi.action.rxAction
import com.xlong.mvi.data.MutableObservableList
import com.xlong.mvi.data.ObservableList
import com.xlong.mvi.vm.RxViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

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

    private val followReducer = ResponseStateReducer<Any, List<FollowModel>>()
    private val actionFollowReducer = ResponseStateReducer<Pair<String, Int>, Any>() // Pair(suid,follow)
    private var followPage = 1
    private val _followList = MutableObservableList<FollowModel>()
    val followList: ObservableList<FollowModel> = _followList

    val myGiftReducer = ResponseStateReducer<Any, MyGiftModel>()
    private val _myGiftList = MutableObservableList<UserGiftModel>()
    val myGiftList: ObservableList<UserGiftModel> = _myGiftList
    val myGiftSubject = PublishSubject.create<MyGiftModel>()

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

        actionFollowReducer.observable.filter { it.isFail || it.isSuccess }.observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (it.isSuccess) {
                it.metadata?.let { pair ->
                    val index = _followList.indexOfFirst { it.uid == pair.first }
                    if (index != -1) {
                        val model = _followList[index]
                        model.is_follow = pair.second
                        _followList[index] = model
                    }
                }
            }
        }

        myGiftReducer.observable.filter { it.isFail || it.isSuccess }.subscribe {
            if (it.isSuccess || it.isFail) {// TODO: 2022/11/27
                it.data?.let { myGiftModel ->
                    myGiftSubject.onNext(myGiftModel)
                    if (!myGiftModel.list.isNullOrEmpty()) {
                        _myGiftList.reset(myGiftModel.list!!)
                    }
                }

                addTestMyGift(_myGiftList)//// TODO: 2022/11/27
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

    /**
     * 关注列表
     */
    fun getFollowList(refresh: Boolean, key: String) {
        if (refresh) {
            followPage = 1
        }
        rxAction<BaseModel<List<FollowModel>>> {
            observable = ApiClient.getInstance().basicService.getFollowList(followPage, key)
            token = "getFollowList"
            dispatcher = followReducer
            deDuper = mDeduper
        }.execute()

        addTestFollow(_followList) // TODO: 2022/11/27
    }

    /**
     * 关注操作
     * suid ： 关注/取消关注 的对象
     * follow 1-关注，0-取消关注
     */
    fun actionFollow(suid: String, follow: Int) {
        rxAction<BaseModel<Any>> {
            observable = ApiClient.getInstance().basicService.actionFollow(suid, follow)
            token = "getPoseLib"
            dispatcher = actionFollowReducer
            deDuper = mDeduper
            metadata = Pair(suid, follow)
        }.execute()
    }

    /**
     * 获取我收到的礼物
     */
    fun getMyGift() {
        rxAction<BaseModel<MyGiftModel>> {
            observable = ApiClient.getInstance().basicService.getMyGift()
            token = "getMyGift"
            dispatcher = myGiftReducer
            deDuper = mDeduper
        }.execute()
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

    private fun addTestFollow(observableList: MutableObservableList<FollowModel>) {
        val avatar = "https://aimg.tangdou.com/public/avatar/2019/0823/p9739530.jpg!small"
        val model1 = FollowModel("1", "1231", "李教练1", avatar, 0, 0)
        val model2 = FollowModel("2", "1232", "李教练2", avatar, 11, 1)
        val model3 = FollowModel("3", "1233", "李教练3", avatar, 22, 1)
        val model4 = FollowModel("4", "1234", "李教练4", avatar, 33, 1)
        val model5 = FollowModel("5", "1235", "李教练5", avatar, 44, 0)
        val model6 = FollowModel("6", "1236", "李教练6", avatar, 55, 0)
        val model7 = FollowModel("7", "1237", "李教练7", avatar, 66, 0)
        val model8 = FollowModel("8", "1238", "李教练8", avatar, 77, 0)
        val model9 = FollowModel("9", "1239", "李教练9", avatar, 99, 0)
        val model10 = FollowModel("10", "12310", "李教练10", avatar, 110, 0)
        val model11 = FollowModel("11", "12311", "李教练11", avatar, 222, 0)

        val list = mutableListOf<FollowModel>()
        list.add(model1)
        list.add(model2)
        list.add(model3)
        list.add(model4)
        list.add(model5)
        list.add(model6)
        list.add(model7)
        list.add(model8)
        list.add(model9)
        list.add(model10)
        list.add(model11)
        observableList.addAll(list)
    }

    private fun addTestMyGift(observableList: MutableObservableList<UserGiftModel>) {
        val avatar = "https://aimg.tangdou.com/public/avatar/2019/0823/p9739530.jpg!small"
        val model1 = UserGiftModel("1", "李教练", "1", avatar, 1, 1, 1, 1, 1, 1, 1)
        val model2 = UserGiftModel("2", "李教练李教练李教练", "2", avatar, 3, 3, 3, 3, 3, 3, 3)
        val model3 = UserGiftModel("3", "李教练李教练", "3", avatar, 11, 11, 11, 11, 11, 11, 11)
        val model4 = UserGiftModel("4", "李教练李教练", "4", avatar, 66, 66, 66, 66, 66, 66, 66)
        val model5 = UserGiftModel("5", "李教练", "5", avatar, 33, 33, 33, 33, 33, 33, 33)
        val model6 = UserGiftModel("6", "李教练李教练", "6", avatar, 5, 5, 5, 5, 5, 5, 5)
        val model7 = UserGiftModel("7", "李教练", "7", avatar, 12345, 12345, 12345, 12345, 12345, 12345, 12345)
        val model8 = UserGiftModel("8", "李教练", "8", avatar, 15, 15, 15, 15, 15, 15, 15)
        val model9 = UserGiftModel("9", "李教练", "9", avatar, 15, 15, 23, 23, 23, 23, 23)
        val model10 = UserGiftModel("10", "李教练李教练", "10", avatar, 23, 88, 88, 88, 88, 88, 88)
        val model11 = UserGiftModel("11", "李教练李教练", "11", avatar, 123, 123, 123, 123, 123, 123, 123)
        val model12 = UserGiftModel("12", "李教练李教练", "12", avatar, 1234, 1234, 1234, 1234, 1234, 1234, 1234)

        val list = mutableListOf<UserGiftModel>()
        list.add(model1)
        list.add(model2)
        list.add(model3)
        list.add(model4)
        list.add(model5)
        list.add(model6)
        list.add(model7)
        list.add(model8)
        list.add(model9)
        list.add(model10)
        list.add(model11)
        list.add(model12)
        observableList.addAll(list)

        val myGiftModel = MyGiftModel(12,123,33,66,12345,12745,126656,list)
        myGiftSubject.onNext(myGiftModel)
    }

}