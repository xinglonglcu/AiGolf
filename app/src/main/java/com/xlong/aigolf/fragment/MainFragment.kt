package com.xlong.aigolf.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R
import com.xlong.aigolf.delegate.MainVideoDelegate
import com.xlong.aigolf.utils.UIUtils
import com.xlong.aigolf.viewmodel.MainViewModel
import com.xlong.libui.pullToRef.SuperSwipeRefreshLayout
import com.xlong.mvi.adapter.ReactiveAdapter
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * 主页
 * Create by xlong 2022/11/23
 */
class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getVideoList(true)
    }

    private fun initView() {
        val delegate = MainVideoDelegate(viewModel.videoList)
        recyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerview.adapter = ReactiveAdapter(delegate, this)
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
                outRect.bottom = UIUtils.dp2px(5f)
            }
        })

        layout_swipe.setHeaderViewBackgroundColor(resources.getColor(R.color.c_262626))

        layout_swipe.setOnPullRefreshListener(object : SuperSwipeRefreshLayout.OnPullRefreshListener {
            override fun onRefresh() {
                Log.i(TAG, "onRefresh: ")
                viewModel.getVideoList(true)
            }

            override fun onPullDistance(distance: Int) {
                Log.i(TAG, "onPullDistance:2 $distance")
            }

            override fun onPullEnable(enable: Boolean) {
                Log.i(TAG, "onPullEnable:2 $enable")
            }
        })
        layout_swipe.setOnPushLoadMoreListener(object : SuperSwipeRefreshLayout.OnPushLoadMoreListener {
            override fun onLoadMore() {
                Log.i(TAG, "onLoadMore: ")
            }

            override fun onPushDistance(distance: Int) {
                Log.i(TAG, "onPushDistance:1 $distance")
            }

            override fun onPushEnable(enable: Boolean) {
                Log.i(TAG, "onPushEnable:1 $enable")
            }
        })

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.i(TAG, "onScrollStateChanged: newState = $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.i(TAG, "onScrolled: dx:$dx, dy:$dy;  ")
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun onBackPressed(): Boolean {
        if (layout_swipe.isRefreshing) {
            layout_swipe.isRefreshing = false
            return true
        }
        return false
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

}