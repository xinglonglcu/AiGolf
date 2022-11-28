package com.xlong.aigolf.delegate

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.xlong.aigolf.R
import com.xlong.data.model.MyInfoModel
import com.xlong.mvi.adapter.ItemDelegate
import com.xlong.mvi.adapter.UnbindableVH
import io.reactivex.Observable
import kotlinx.android.synthetic.main.layout_mine_home_header.view.*

/**
 * 我的-首页-上方信息部分
 * Create by xlong 2022/11/28
 */
class HomeHeaderDelegate(data: Observable<MyInfoModel>, val onTabSelect: OnTabSelectCallback? = null) : ItemDelegate<Observable<MyInfoModel>>(data) {

    override val layoutRes: Int
        get() = R.layout.layout_mine_home_header

    override fun onCreateVH(parent: ViewGroup, layoutRes: Int): UnbindableVH<Observable<MyInfoModel>> {
        return InfoVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    inner class InfoVH(val view: View) : UnbindableVH<Observable<MyInfoModel>>(view) {
        override fun onBind(data: Observable<MyInfoModel>) {
            data.subscribe {

                view.tv_pose_num.text = "${it.pose_lib_num}"
                view.tv_collect_num.text = "${it.collect_num}"
                view.tv_share_num.text = "${it.share_num}"
                view.tv_gift_num.text = "${it.gift_num}"

                view.layout_record.setOnClickListener {
                    Toast.makeText(context, "layout_record", Toast.LENGTH_SHORT).show()
                }
                view.layout_upload.setOnClickListener {
                    Toast.makeText(context, "layout_upload", Toast.LENGTH_SHORT).show()
                }
                view.layout_pose.setOnClickListener {
                    Toast.makeText(context, "layout_pose", Toast.LENGTH_SHORT).show()
                }
                view.layout_collect.setOnClickListener {
                    Toast.makeText(context, "layout_collect", Toast.LENGTH_SHORT).show()
                }
                view.layout_share.setOnClickListener {
                    Toast.makeText(context, "layout_share", Toast.LENGTH_SHORT).show()
                }
                view.layout_gift.setOnClickListener {
                    Toast.makeText(context, "layout_gift", Toast.LENGTH_SHORT).show()
                }

                view.tv_videos.text = String.format(context.resources.getString(R.string.info_video), "${it.video_num}")
                view.tv_analysis.text = String.format(context.resources.getString(R.string.info_analysis), "${it.analysis_num}")
                view.tv_compare.text = String.format(context.resources.getString(R.string.info_compare), "${it.compare_num}")
                view.tv_report.text = String.format(context.resources.getString(R.string.info_report), "${it.report_num}")

                view.layout_video.setOnClickListener {
                    setTabStatus(view.tv_videos, true)
                    setTabStatus(view.tv_analysis, false)
                    setTabStatus(view.tv_compare, false)
                    setTabStatus(view.tv_report, false)
                    onTabSelect?.onVideo()
                }
                view.layout_analysis.setOnClickListener {
                    setTabStatus(view.tv_videos, false)
                    setTabStatus(view.tv_analysis, true)
                    setTabStatus(view.tv_compare, false)
                    setTabStatus(view.tv_report, false)
                    onTabSelect?.onAnyalysis()
                }
                view.layout_compare.setOnClickListener {
                    setTabStatus(view.tv_videos, false)
                    setTabStatus(view.tv_analysis, false)
                    setTabStatus(view.tv_compare, true)
                    setTabStatus(view.tv_report, false)
                    onTabSelect?.onCompare()
                }
                view.layout_report.setOnClickListener {
                    setTabStatus(view.tv_videos, false)
                    setTabStatus(view.tv_analysis, false)
                    setTabStatus(view.tv_compare, false)
                    setTabStatus(view.tv_report, true)
                    onTabSelect?.onReport()
                }
                view.tv_videos.callOnClick()
            }.autoDispose()
        }

        private fun setTabStatus(textview: TextView, active: Boolean) {
            if (active) {
                textview.setTextColor(context.resources.getColor(R.color.c_ffffff))
                textview.setTypeface(null, Typeface.BOLD)
            } else {
                textview.setTextColor(context.resources.getColor(R.color.c_cdcdcd))
                textview.setTypeface(null, Typeface.NORMAL)
            }
            when (textview.id) {
                view.tv_videos.id -> view.indicator_video.visibility = if (active) View.VISIBLE else View.GONE
                view.tv_analysis.id -> view.indicator_analysis.visibility = if (active) View.VISIBLE else View.GONE
                view.tv_compare.id -> view.indicator_compare.visibility = if (active) View.VISIBLE else View.GONE
                view.tv_report.id -> view.indicator_report.visibility = if (active) View.VISIBLE else View.GONE
                else -> {

                }
            }
        }
    }

    interface OnTabSelectCallback {
        fun onVideo()
        fun onAnyalysis()
        fun onCompare()
        fun onReport()
    }
}