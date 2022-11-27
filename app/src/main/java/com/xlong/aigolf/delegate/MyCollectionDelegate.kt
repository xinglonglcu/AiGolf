package com.xlong.aigolf.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.ScreenUtils
import com.xlong.aigolf.utils.StringUtils
import com.xlong.aigolf.utils.UIUtils
import com.xlong.data.model.VideoModel
import com.xlong.libui.imagload.ImageLoader
import com.xlong.mvi.adapter.ListDelegate
import com.xlong.mvi.adapter.UnbindableVH
import com.xlong.mvi.data.ObservableList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_main_video.view.*

/**
 * 我的收藏
 * Create by xlong 2022/11/25
 */
class MyCollectionDelegate(list: ObservableList<VideoModel>) : ListDelegate<VideoModel>(list) {

    private val itemWidth = (ScreenUtils.getFullScreenWidth() - UIUtils.dp2px(5 * 3f)) * 1.0f / 2
    private val itemCoverH = itemWidth * 4 / 3

    override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<VideoModel> {
        return VideoVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    override fun getLayoutRes(position: Int): Int = R.layout.item_main_video

    private inner class VideoVH(val view: View) : UnbindableVH<VideoModel>(view), LayoutContainer {
        override val containerView: View = view

        override fun onBind(data: VideoModel) {

            val layoutParams = view.iv_cover.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.width = itemWidth.toInt()
            layoutParams.height = itemCoverH.toInt()

            if (!data.cover.isNullOrEmpty()) {
                ImageLoader.load(context, data.cover!!).into(view.iv_cover)
            }
            view.tv_title.text = data.title
            view.tv_name.text = data.name
            data.avatar?.let {
                ImageLoader.load(context, data.avatar!!).into(view.iv_avatar)
            }
            view.tv_heart_num.text = "${StringUtils.convertToWan(data.heart_num)}"
            if (data.is_heart == 1) {
                view.iv_heart.setImageResource(R.mipmap.ic_heart)
            } else {
                view.iv_heart.setImageResource(R.mipmap.ic_hearted)
            }

            view.root.setOnClickListener {
                Toast.makeText(context, "${data.title}:${data.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}