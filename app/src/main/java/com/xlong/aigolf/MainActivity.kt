package com.xlong.aigolf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.xlong.aigolf.fragment.MainFragment
import com.xlong.aigolf.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页
 * Created by xlong on 2022/11/23
 */
class MainActivity : BaseActivity() {

    private val mineFragment: MineFragment by lazy { MineFragment.newInstance() }
    private val mainFragment: MainFragment by lazy { MainFragment.newInstance() }
    private val tabs = arrayOf("视频", "我的")
    private val fragmentList: MutableList<BaseFragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: --- ${System.currentTimeMillis()}")
        initView()
    }

    private fun initView() {
        fragmentList.add(mainFragment)
        fragmentList.add(mineFragment)

        view_pager.currentItem = 0
        view_pager.offscreenPageLimit = 2
        view_pager.adapter = MyAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.getTabAt(0)?.customView = getTabView(0)
        tab_layout.getTabAt(1)?.customView = getTabView(1)

        iv_msg.setOnClickListener { Toast.makeText(this, "消息", Toast.LENGTH_SHORT).show() }
        iv_scan.setOnClickListener { Toast.makeText(this, "扫码", Toast.LENGTH_SHORT).show() }
        iv_search.setOnClickListener { Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show() }
    }

    private fun getTabView(pos: Int): View {
        val view = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null)
        val textView = view.findViewById<TextView>(R.id.tv_title)
        textView.text = tabs[pos]
        return view
    }

    inner class MyAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItemPosition(obj: Any): Int {
            return POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return 2
        }
    }
}