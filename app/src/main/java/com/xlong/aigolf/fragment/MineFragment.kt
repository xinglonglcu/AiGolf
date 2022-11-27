package com.xlong.aigolf.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R

/**
 * 我的页
 * Created by xlong on 2022/11/23
 */
class MineFragment : BaseFragment() {

    private val homeFragment: HomeFragment by lazy { HomeFragment.newInstance() }
    private val aiRecordFragment: AiRecordFragment by lazy { AiRecordFragment.newInstance() }
    private val aiReportFragment: AiReportFragment by lazy { AiReportFragment.newInstance() }
    private val aiCompareFragment: AiCompareFragment by lazy { AiCompareFragment.newInstance() }
    private val personalFragment: PersonalFragment by lazy { PersonalFragment.newInstance() }
    private val fragmentList = mutableListOf<BaseFragment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navView = getMyActivity().findViewById<BottomNavigationView>(R.id.nav_view)
//        val navController = (getMyActivity() as Activity).findNavController(R.id.nav_host_fragment_activity_main)
//        navView.setupWithNavController(navController)
//        navView.itemIconTintList = null

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    Log.d(TAG, "OnItemSelected: ---navigation_home")
                    showFragment(0)
                }
                R.id.navigation_ai_record -> {
                    Log.d(TAG, "OnItemSelected: ---navigation_ai_record")
                    showFragment(1)
                }
                R.id.navigation_ai_compare -> {
                    Log.d(TAG, "OnItemSelected: ---navigation_ai_compare")
                    showFragment(2)
                }
                R.id.navigation_ai_report -> {
                    Log.d(TAG, "OnItemSelected: ---navigation_ai_report")
                    showFragment(3)
                }
                R.id.navigation_personal -> {
                    Log.d(TAG, "OnItemSelected: ---navigation_personal")
                    showFragment(4)
                }
                else -> {

                }
            }
            true
        }
        navView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    Log.d(TAG, "OnItemReselected: ---navigation_home")
                    refreshFragment(0)
                }
                R.id.navigation_ai_record -> {
                    Log.d(TAG, "OnItemReselected: ---navigation_ai_record")
                    refreshFragment(1)
                }
                R.id.navigation_ai_compare -> {
                    Log.d(TAG, "OnItemReselected: ---navigation_ai_compare")
                    refreshFragment(2)
                }
                R.id.navigation_ai_report -> {
                    Log.d(TAG, "OnItemReselected: ---navigation_ai_report")
                    refreshFragment(3)
                }
                R.id.navigation_personal -> {
                    Log.d(TAG, "OnItemReselected: ---navigation_personal")
                    refreshFragment(4)
                }
                else -> {

                }
            }
            true
        }
        initFragment()
    }

    private fun initFragment() {
        fragmentList.add(homeFragment)
        fragmentList.add(aiRecordFragment)
        fragmentList.add(aiCompareFragment)
        fragmentList.add(aiReportFragment)
        fragmentList.add(personalFragment)
        showFragment(0)
    }

    private fun showFragment(pos: Int) {
        val transaction = getMyActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentList[pos])
        transaction.commitAllowingStateLoss()
    }

    private fun refreshFragment(pos: Int) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!childFragmentManager.fragments.isNullOrEmpty()) {
            for (fragment in childFragmentManager.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}