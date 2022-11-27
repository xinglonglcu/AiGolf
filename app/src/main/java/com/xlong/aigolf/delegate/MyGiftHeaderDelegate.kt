package com.xlong.aigolf.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.StringUtils
import com.xlong.data.model.MyGiftModel
import com.xlong.mvi.adapter.ItemDelegate
import com.xlong.mvi.adapter.UnbindableVH
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_my_gift_header.view.*

/**
 *
 * Create by xlong 2022/11/27
 */
class MyGiftHeaderDelegate(observer: Observable<MyGiftModel>) : ItemDelegate<Observable<MyGiftModel>>(observer) {
    override val layoutRes: Int
        get() = R.layout.item_my_gift_header

    override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<Observable<MyGiftModel>> {
        return GiftVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    inner class GiftVH(val view: View) : UnbindableVH<Observable<MyGiftModel>>(view) {
        override fun onBind(data: Observable<MyGiftModel>) {
            data.subscribe { giftModel ->
                view.tv_golf_num.text = "${StringUtils.convertToWan(giftModel.golf_total_num)}"
                view.tv_golden_num.text = "${StringUtils.convertToWan(giftModel.golden_total_num)}"
                view.tv_silver_num.text = "${
                    StringUtils.convertToWan(giftModel.silver_total_num)
                }"
                view.tv_iron_num.text = "${
                    StringUtils.convertToWan(giftModel.iron_pole_total_num)
                }"
                view.tv_wooden_num.text = "${
                    StringUtils.convertToWan(giftModel.wooden_pole_total_num)
                }"
                view.tv_training_num.text = "${
                    StringUtils.convertToWan(giftModel.training_total_num)
                }"
                view.tv_ball_ground_num.text = "${
                    StringUtils.convertToWan(giftModel.ball_ground_total_num)
                }"
            }
        }
    }
}