package com.xlong.aigolf.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R

/**
 * 个人中心
 * Created by xlong on 2022/11/23
 */
class PersonalFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_personal, container, false)
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
        fun newInstance(): PersonalFragment {
            return PersonalFragment()
        }
    }
}