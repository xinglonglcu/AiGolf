package com.xlong.aigolf.activity

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.delegate.PoseLibDelegate
import com.xlong.aigolf.fragment.PoseLibBottomFilterFragment
import com.xlong.aigolf.utils.ScreenUtils
import com.xlong.aigolf.utils.UIUtils
import com.xlong.aigolf.viewmodel.MainViewModel
import com.xlong.mvi.adapter.ReactiveAdapter
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_pose_lib.*
import java.util.concurrent.TimeUnit

/**
 * 姿势库
 * Create by xlong 2022/11/25
 */
class PoseLibActivity : BaseActivity() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    private val searchSubject = PublishSubject.create<String>()
    private var disposable: Disposable? = null

    //key -    搜索词
    //type -   类型： 0-全部，1-专业球员，2-教练
    //gender - 性别： 0-全部，1-女，2-男
    //action   动作:  0-全部，1-全挥杆，2-半挥杆，3-小挥杆
    //pole     球杆： 0-全部，1-木杆，2-铁杆，3-推杆
    //holder    持杆手：0-全部，1-左手，2-右手
    //direction 方向:0-搜索，1-正面，2-侧面
    private var paramMap = mutableMapOf(KEY to "", TYPE to "0", GENDER to "0", ACTION to "0", POLE to "0", HOLDER to "0", DIRECTION to "0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.setStatusBarColor(this, R.color.c_181818)
        setContentView(R.layout.activity_pose_lib)
        initView()
        disposable = searchSubject.debounce(1000, TimeUnit.MILLISECONDS).subscribe {
            paramMap["key"] = it//搜索结果
            viewModel.getPoseLibList(false, paramMap)
        }

        viewModel.getPoseLibList(true, paramMap)
    }

    private fun initView() {
        iv_back.setOnClickListener { finish() }
        layout_filter.setOnClickListener {
            val filterFragment = PoseLibBottomFilterFragment(this, paramMap) {
                Log.d(TAG, "initView:  ${it}")
                Log.d(TAG, "initView:  ${paramMap}")
                viewModel.getPoseLibList(false, paramMap)
            }
            filterFragment.show(supportFragmentManager, "filterFragment")
            Toast.makeText(this, "过滤", Toast.LENGTH_SHORT).show()
        }

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

        val delegate = PoseLibDelegate(viewModel.poseList)
        recyclerview.adapter = ReactiveAdapter(delegate, this)
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = UIUtils.dp2px(7f)
                outRect.bottom = UIUtils.dp2px(7f)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    companion object {
        //筛选类型
        const val KEY = "key"//搜索
        const val TYPE = "type"//类型
        const val GENDER = "gender"//性别
        const val ACTION = "action"//动作
        const val POLE = "pole"//球杆
        const val HOLDER = "holder"//持杆手
        const val DIRECTION = "direction"//方向
    }
}