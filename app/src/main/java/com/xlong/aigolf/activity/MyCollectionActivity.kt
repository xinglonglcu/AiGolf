package com.xlong.aigolf.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.delegate.MyCollectionDelegate
import com.xlong.aigolf.utils.ScreenUtils
import com.xlong.aigolf.utils.UIUtils
import com.xlong.aigolf.viewmodel.MainViewModel
import com.xlong.mvi.adapter.ReactiveAdapter
import kotlinx.android.synthetic.main.activity_my_collection.*

/**
 * 我的收藏页
 * Create by xlong 2022/11/25
 */
class MyCollectionActivity : BaseActivity() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_collection)
        ScreenUtils.setStatusBarColor(this, R.color.c_181818)

        iv_back.setOnClickListener {
            finish()
        }
        recyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val delegete = MyCollectionDelegate(viewModel.collectList)
        recyclerview.adapter = ReactiveAdapter(delegete, this)
        recyclerview.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                if (params.isFullSpan) {
                    // 有headerView时isFullSpan=true 不单独设置边距
                    return
                }
                // 两列 %2
                if (params.spanIndex % 2 == 0) {
                    // 左列
                    outRect.left = UIUtils.dp2px(5f)
                    outRect.right = UIUtils.dp2px(2.5f)
                } else {
                    // 右列
                    outRect.left = UIUtils.dp2px(2.5f)
                    outRect.right = UIUtils.dp2px(5f)
                }
                outRect.top = UIUtils.dp2px(5f)
            }
        })
        viewModel.getMyCollection(true)
    }
}