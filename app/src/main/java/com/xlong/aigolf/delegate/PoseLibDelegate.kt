package com.xlong.aigolf.delegate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
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
import kotlinx.android.synthetic.main.item_pose_lib.view.*

/**
 * 姿识库
 * Create by xlong 2022/11/25
 */
class PoseLibDelegate(list: ObservableList<VideoModel>) : ListDelegate<VideoModel>(list) {

    private val itemWidth = ScreenUtils.getFullScreenWidth() - 2 * UIUtils.dp2px(15f)
    private val itemHeight = (144 * 1.0f * itemWidth / 329).toInt()

    override fun getLayoutRes(position: Int): Int = R.layout.item_pose_lib
    override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<VideoModel> {
        return PoseVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    inner class PoseVH(val view: View) : UnbindableVH<VideoModel>(view) {
        override fun onBind(data: VideoModel) {

            val params = view.container.layoutParams
            params.width = itemWidth
            params.height = itemHeight
            view.container.layoutParams = params

            val paramsCover = view.iv_cover.layoutParams as ConstraintLayout.LayoutParams
            paramsCover.width = itemWidth
            paramsCover.height = itemHeight
            view.iv_cover.layoutParams = paramsCover

            if (!data.cover.isNullOrEmpty()) {
                ImageLoader.load(context, data.cover!!).into(view.iv_cover)
            }
            if (!data.avatar.isNullOrEmpty()) {
                ImageLoader.load(context, data.avatar!!).error(R.mipmap.default_head).placeholder(R.mipmap.default_head).into(view.iv_avatar)
            }
            if (!data.name.isNullOrEmpty()) {
                view.tv_name.text = data.name
            }
            if (!data.title.isNullOrEmpty()) {
                view.tv_title.text = data.title
            }
            val sb = StringBuffer()
            sb.append(StringUtils.millsecondsToStr1((data.duration * 1000).toInt(), false)).append(" • ")
            sb.append(String.format(context.resources.getString(R.string.compare_num), data.compare_num))
            view.tv_duration_compare.text = sb.toString()
            view.root.setOnClickListener { Toast.makeText(context, "${data.name}:${data.title}", Toast.LENGTH_SHORT).show() }
        }
    }
}