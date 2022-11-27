package com.xlong.aigolf.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.delegate.MyGiftDelegate
import com.xlong.aigolf.delegate.MyGiftHeaderDelegate
import com.xlong.aigolf.viewmodel.MainViewModel
import com.xlong.mvi.adapter.ReactiveAdapter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_my_follow.*

/**
 * 我的礼物
 * Create by xlong 2022/11/27
 */
class MyGiftActivity : BaseActivity() {

    private var disposable: Disposable? = null
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_gift)

        val delegate = MyGiftDelegate(viewModel.myGiftList)
        val adapter = ReactiveAdapter(delegate, this)
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter
        val headerDelegate = MyGiftHeaderDelegate(viewModel.myGiftSubject)
        adapter.addHeader(headerDelegate)
        viewModel.getMyGift()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}