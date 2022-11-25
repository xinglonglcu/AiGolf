package com.xlong.aigolf.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R
import com.xlong.aigolf.viewmodel.HomeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * 首页
 * Created by xlong on 2022/11/23
 */
class HomeFragment : BaseFragment() {

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
        tv_test.setOnClickListener {
            viewModel.test()
        }
        tv_login.setOnClickListener {
            viewModel.login("123","456")
        }
    }

    private fun initObserver() {
        viewModel.accountSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.d(TAG, "initObserver: ---- $it")
        }
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