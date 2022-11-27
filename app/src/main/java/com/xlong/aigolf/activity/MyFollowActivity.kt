package com.xlong.aigolf.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.delegate.MyFollowDelegate
import com.xlong.aigolf.viewmodel.MainViewModel
import com.xlong.data.model.FollowModel
import com.xlong.mvi.adapter.ReactiveAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_my_follow.*
import java.util.concurrent.TimeUnit

/**
 * 我的关注
 * Create by xlong 2022/11/27
 */
class MyFollowActivity : BaseActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val searchSubject = PublishSubject.create<String>()
    private var disposable: Disposable? = null
    private var searchKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_follow)

        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val delegate = MyFollowDelegate(viewModel.followList, object : MyFollowDelegate.OnCallback {
            override fun onLetterClick(data: FollowModel, pos: Int) {
                Toast.makeText(this@MyFollowActivity, "私信：${data.name} : ${data.letter_num}", Toast.LENGTH_SHORT).show()
            }

            override fun onCheckClick(data: FollowModel, pos: Int) {
                Toast.makeText(this@MyFollowActivity, "查看：${data.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onFollowClick(data: FollowModel, pos: Int) {
                Toast.makeText(this@MyFollowActivity, "关注：${data.name} : ${data.is_follow}", Toast.LENGTH_SHORT).show()
                viewModel.actionFollow(data.uid!!, if (data.is_follow == 0) 1 else 0)
            }
        })
        recyclerview.adapter = ReactiveAdapter(delegate, this)

        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val key = s.toString().replace(" ", "").trim()
                Log.i(TAG, "afterTextChanged: $key")
                searchSubject.onNext(key)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        disposable = searchSubject.debounce(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe({
            searchKey = it//搜索结果
            viewModel.getFollowList(true, it)
        }, {
            it.printStackTrace()
            Log.e(TAG, "searchSubject: ${it.message}")
        })

        viewModel.getFollowList(true, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}