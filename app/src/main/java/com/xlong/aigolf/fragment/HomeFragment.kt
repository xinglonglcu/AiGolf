package com.xlong.aigolf.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R
import com.xlong.aigolf.delegate.HomeDelegate
import com.xlong.aigolf.delegate.HomeHeaderDelegate
import com.xlong.aigolf.viewmodel.HomeViewModel
import com.xlong.data.model.MyVideoType
import com.xlong.mvi.adapter.ReactiveAdapter
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * 首页
 * Created by xlong on 2022/11/23
 */
class HomeFragment : BaseFragment() {

    private var currentVideoType = MyVideoType.MYVIDEO.type
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()
    }

    private fun initView() {
        val delegate = HomeDelegate(viewModel.myVideos)
        val adapter = ReactiveAdapter(delegate, getMyActivity())
        recyclerview.layoutManager = LinearLayoutManager(getMyActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter
        adapter.addHeader(HomeHeaderDelegate(viewModel.myInfoSubject, object : HomeHeaderDelegate.OnTabSelectCallback {
            override fun onVideo() {
                Log.d(TAG, "onVideo: ")
            }

            override fun onAnyalysis() {
                Log.d(TAG, "onAnyalysis: ")
            }

            override fun onCompare() {
                Log.d(TAG, "onCompare: ")
            }

            override fun onReport() {
                Log.d(TAG, "onReport: ")
            }
        }))
        viewModel.getMyVideos(currentVideoType)
        viewModel.getMyInfo()
    }

    private fun initObserver() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}