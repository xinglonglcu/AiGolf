package com.xlong.aigolf.delegate

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.ScreenUtils
import com.xlong.aigolf.utils.StringUtils
import com.xlong.aigolf.utils.UIUtils
import com.xlong.data.model.MyVideoModel
import com.xlong.data.model.VideoModel
import com.xlong.libui.imagload.ImageLoader
import com.xlong.mvi.adapter.ListDelegate
import com.xlong.mvi.adapter.ReactiveAdapter
import com.xlong.mvi.adapter.UnbindableVH
import com.xlong.mvi.data.MutableObservableList
import com.xlong.mvi.data.ObservableList
import kotlinx.android.synthetic.main.item_mine_home.view.*
import kotlinx.android.synthetic.main.item_mine_home_video.view.*

/**
 * 我的-首页
 * Create by xlong 2022/11/28
 */
class HomeDelegate(list: ObservableList<MyVideoModel>) : ListDelegate<MyVideoModel>(list) {

    private val space = UIUtils.dp2px(6f)
    private val itemWidth = ((ScreenUtils.getFullScreenWidth() - UIUtils.dp2px(15 * 2f) - space * 4) * 1.0f / 3).toInt()//去除左右间距及item间距

    override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<MyVideoModel> {
        return CollectVideoVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    override fun getLayoutRes(position: Int): Int = R.layout.item_mine_home

    inner class CollectVideoVH(val view: View) : UnbindableVH<MyVideoModel>(view) {
        override fun onBind(data: MyVideoModel) {
            view.tv_time.text = data.time

            val videos = MutableObservableList<VideoModel>()
            data.list?.let {
                videos.addAll(data.list!!)
            }
            val delegate = VideoDelegate(videos)
            val adapter = ReactiveAdapter(delegate, context as BaseActivity)
            view.recyclerview.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            view.recyclerview.adapter = adapter

            if (view.recyclerview.itemDecorationCount == 0) {
                view.recyclerview.addItemDecoration(object : RecyclerView.ItemDecoration() {

                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        super.getItemOffsets(outRect, view, parent, state)

                        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                        if (params.isFullSpan) {
                            return
                        }

                        val dataCount = adapter.dataCount
                        val pos = parent.getChildAdapterPosition(view)

                        Log.d("Xlong", "getItemOffsets: dataCount = $dataCount, spanIndex: ${params.spanIndex}  pos = $pos  itemWidth = $itemWidth")

                        outRect.set(0, 0, 0, 0)
                        if (params.spanIndex % 3 == 1) {//中间
                            outRect.left = space - UIUtils.dp2px(2f)
                            outRect.right = space - UIUtils.dp2px(2f)
                        } else if (params.spanIndex % 3 == 2) {//右侧
                            outRect.left = space
                            outRect.right = 0
                        } else {//左侧
                            outRect.left = 0
                            outRect.right = space
                        }
                        if (pos >= 3) {
                            outRect.top = space
                        } else {
                            outRect.top = 0
                        }
                        if (pos < dataCount - dataCount % 3) {
                            outRect.bottom = space
                        } else {
                            outRect.bottom = 0
                        }
                    }
                })
            }
        }
    }

    inner class VideoDelegate(list: ObservableList<VideoModel>) : ListDelegate<VideoModel>(list) {
        override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<VideoModel> {
            return VideoVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
        }

        override fun getLayoutRes(position: Int): Int = R.layout.item_mine_home_video
    }

    inner class VideoVH(val view: View) : UnbindableVH<VideoModel>(view) {
        override fun onBind(data: VideoModel) {
            view.tv_duration.text = StringUtils.millsecondsToStr((data.duration * 1000).toInt())

            val layout = view.cardview.layoutParams
            layout.width = itemWidth
            layout.height = itemWidth
            view.cardview.layoutParams = layout

            data.cover?.let {
                ImageLoader.load(context, data.cover!!).into(view.iv_cover)
            }
            view.root.setOnClickListener {
                Toast.makeText(context, "${data.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}