package com.xlong.aigolf.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.IntentUtils
import kotlinx.android.synthetic.main.fragment_ai_record.*

/**
 * Ai拍
 * Created by xlong on 2022/11/23
 */
class AiRecordFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_ai_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        tv_login.setOnClickListener {
            Toast.makeText(getMyActivity(), "tv_login", Toast.LENGTH_SHORT).show()
            IntentUtils.startLoginActivity(getMyActivity())
        }
        tv_verify.setOnClickListener {
            Toast.makeText(getMyActivity(), "tv_verify", Toast.LENGTH_SHORT).show()
            IntentUtils.startPhoneVerifyActivity(getMyActivity(), "12345678900")
        }
        tv_user_info.setOnClickListener {
            Toast.makeText(getMyActivity(), "tv_user_info", Toast.LENGTH_SHORT).show()
            IntentUtils.startUserInfoModifiActivity(getMyActivity())
        }
        tv_my_collect.setOnClickListener {
            Toast.makeText(getMyActivity(), "tv_my_collect", Toast.LENGTH_SHORT).show()
            IntentUtils.startMyCollectionActivity(getMyActivity())
        }

        tv_pose_lib.setOnClickListener {
            Toast.makeText(getMyActivity(), "tv_pose_lib", Toast.LENGTH_SHORT).show()
            IntentUtils.startPoseLibActivity(getMyActivity())
        }

        tv_my_follow.setOnClickListener {
            Toast.makeText(getMyActivity(), "tv_my_follow", Toast.LENGTH_SHORT).show()
            IntentUtils.startMyFollowActivity(getMyActivity())
        }
        tv_my_gift.setOnClickListener {
            Toast.makeText(getMyActivity(), "tv_my_gift", Toast.LENGTH_SHORT).show()
            IntentUtils.startMyGiftActivity(getMyActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): AiRecordFragment {
            return AiRecordFragment()
        }
    }
}