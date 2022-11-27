package com.xlong.aigolf.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xlong.aigolf.R
import com.xlong.data.model.UserGiftModel
import com.xlong.libui.imagload.ImageLoader
import com.xlong.mvi.adapter.ListDelegate
import com.xlong.mvi.adapter.UnbindableVH
import com.xlong.mvi.data.ObservableList
import kotlinx.android.synthetic.main.item_my_gift.view.*

/**
 * 收到的礼物
 * Create by xlong 2022/11/27
 */
class MyGiftDelegate(list: ObservableList<UserGiftModel>) : ListDelegate<UserGiftModel>(list) {

    override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<UserGiftModel> {
        return GiftVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    override fun getLayoutRes(position: Int): Int = R.layout.item_my_gift

    inner class GiftVH(val view: View) : UnbindableVH<UserGiftModel>(view) {
        override fun onBind(data: UserGiftModel) {
            data.avatar?.let { ImageLoader.load(context, data.avatar!!).placeholder(R.mipmap.default_head).error(R.mipmap.default_head).into(view.iv_avatar) }
            view.tv_name.text = data.name

            view.tv_golf_num.text = "${data.golf_num}"
            view.tv_golden_num.text = "${data.golden_num}"
            view.tv_silver_num.text = "${data.silver_num}"
            view.tv_iron_num.text = "${data.iron_pole_num}"
            view.tv_wooden_num.text = "${data.wooden_pole_num}"
            view.tv_training.text = "${data.training_num}"
            view.tv_ball_ground.text = "${data.ball_ground_num}"
        }
    }

}