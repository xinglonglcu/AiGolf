package com.xlong.aigolf.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R

/**
 * Ai报
 * Created by xlong on 2022/11/23
 */
class AiReportFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_ai_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    companion object {
        fun newInstance():AiReportFragment  {
            return AiReportFragment()
        }
    }
}