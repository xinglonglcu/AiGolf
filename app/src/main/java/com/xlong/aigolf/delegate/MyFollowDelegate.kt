package com.xlong.aigolf.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xlong.aigolf.R
import com.xlong.data.model.FollowModel
import com.xlong.libui.TDTextView
import com.xlong.libui.imagload.ImageLoader
import com.xlong.mvi.adapter.ListDelegate
import com.xlong.mvi.adapter.UnbindableVH
import com.xlong.mvi.data.ObservableList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_my_follow.view.*

/**
 *
 * Create by xlong 2022/11/27
 */
class MyFollowDelegate(list: ObservableList<FollowModel>, val onCallback: OnCallback?) : ListDelegate<FollowModel>(list) {
    override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<FollowModel> {
        return FollowVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    override fun getLayoutRes(position: Int): Int = R.layout.item_my_follow

    inner class FollowVH(view: View) : UnbindableVH<FollowModel>(view), LayoutContainer {
        override val containerView: View = view
        override fun onBind(data: FollowModel) {
            data.avatar?.let { ImageLoader.load(context, data.avatar!!).placeholder(R.mipmap.default_head).error(R.mipmap.default_head).into(containerView.iv_avatar) }
            containerView.tv_name.text = data.name
            setFollowActive(containerView.tv_follow, data.is_follow != 1)

            if (data.letter_num > 0) {
                containerView.tv_letter_num.visibility = View.VISIBLE
                if (data.letter_num > 99) {
                    containerView.tv_letter_num.text = "99+"
                } else {
                    containerView.tv_letter_num.text = "${data.letter_num}"
                }
            } else {
                containerView.tv_letter_num.visibility = View.GONE
            }

            containerView.tv_private_letter.setOnClickListener {
                onCallback?.onLetterClick(data, currentPosition)
            }
            containerView.tv_check.setOnClickListener {
                onCallback?.onCheckClick(data, currentPosition)
            }
            containerView.tv_follow.setOnClickListener {
                onCallback?.onFollowClick(data, currentPosition)
            }

        }

        //设置关注按钮状态 active:true 红色-未关注；flase:黑色已关注
        private fun setFollowActive(view: TDTextView, active: Boolean) {
            view.setSolidAndStrokeColor(if (active) containerView.resources.getColor(R.color.c_F82E54) else containerView.resources.getColor(R.color.c_2f2f2f), 0)
            view.text = if (active) "关注" else "已关注"
        }
    }

    interface OnCallback {
        fun onLetterClick(data: FollowModel, pos: Int)
        fun onCheckClick(data: FollowModel, pos: Int)
        fun onFollowClick(data: FollowModel, pos: Int)
    }

}