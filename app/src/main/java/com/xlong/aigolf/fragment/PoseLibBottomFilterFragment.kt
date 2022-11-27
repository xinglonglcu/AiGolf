package com.xlong.aigolf.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xlong.aigolf.R
import com.xlong.aigolf.activity.PoseLibActivity
import com.xlong.libui.TDTextView
import kotlinx.android.synthetic.main.fragment_pose_lib_filter.*

/**
 * 姿识库筛选框
 * Create by xlong 2022/11/26
 */
class PoseLibBottomFilterFragment(val mActivity: FragmentActivity, val params: MutableMap<String, String>, val onCallback: (map: Map<String, String>) -> Unit) : BottomSheetDialogFragment() {

    private val typeViews = mutableListOf<TDTextView>()
    private val genderViews = mutableListOf<TDTextView>()
    private val actionViews = mutableListOf<TDTextView>()
    private val poleViews = mutableListOf<TDTextView>()
    private val holderViews = mutableListOf<TDTextView>()
    private val directionViews = mutableListOf<TDTextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pose_lib_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        typeViews.add(tv_type_all)
        typeViews.add(tv_type_1)
        typeViews.add(tv_type_2)

        genderViews.add(tv_gender_all)
        genderViews.add(tv_gender_1)
        genderViews.add(tv_gender_2)

        actionViews.add(tv_action_all)
        actionViews.add(tv_action_1)
        actionViews.add(tv_action_2)
        actionViews.add(tv_action_3)

        poleViews.add(tv_pole_all)
        poleViews.add(tv_pole_1)
        poleViews.add(tv_pole_2)
        poleViews.add(tv_pole_3)

        holderViews.add(tv_holder_all)
        holderViews.add(tv_holder_1)
        holderViews.add(tv_holder_2)

        directionViews.add(tv_direction_all)
        directionViews.add(tv_direction_1)
        directionViews.add(tv_direction_2)

        setTypeData(params[PoseLibActivity.TYPE]!!)
        setGenderData(params[PoseLibActivity.GENDER]!!)
        setActionData(params[PoseLibActivity.ACTION]!!)
        setPoleData(params[PoseLibActivity.POLE]!!)
        setHolderData(params[PoseLibActivity.HOLDER]!!)
        setDirectionData(params[PoseLibActivity.DIRECTION]!!)

        tv_type_all.setOnClickListener { setTypeData("0") }
        tv_type_1.setOnClickListener { setTypeData("1") }
        tv_type_2.setOnClickListener { setTypeData("2") }
        tv_gender_all.setOnClickListener { setGenderData("0") }
        tv_gender_1.setOnClickListener { setGenderData("1") }
        tv_gender_2.setOnClickListener { setGenderData("2") }
        tv_action_all.setOnClickListener { setActionData("0") }
        tv_action_1.setOnClickListener { setActionData("1") }
        tv_action_2.setOnClickListener { setActionData("2") }
        tv_action_3.setOnClickListener { setActionData("3") }
        tv_pole_all.setOnClickListener { setPoleData("0") }
        tv_pole_1.setOnClickListener { setPoleData("1") }
        tv_pole_2.setOnClickListener { setPoleData("2") }
        tv_pole_3.setOnClickListener { setPoleData("3") }
        tv_holder_all.setOnClickListener { setHolderData("0") }
        tv_holder_1.setOnClickListener { setHolderData("1") }
        tv_holder_2.setOnClickListener { setHolderData("2") }
        tv_direction_all.setOnClickListener { setDirectionData("0") }
        tv_direction_1.setOnClickListener { setDirectionData("1") }
        tv_direction_2.setOnClickListener { setDirectionData("2") }

        tv_cancel.setOnClickListener { dismiss() }
        tv_commit.setOnClickListener {
            params[PoseLibActivity.TYPE] = getSelected(typeViews)
            params[PoseLibActivity.GENDER] = getSelected(genderViews)
            params[PoseLibActivity.ACTION] = getSelected(actionViews)
            params[PoseLibActivity.POLE] = getSelected(poleViews)
            params[PoseLibActivity.HOLDER] = getSelected(holderViews)
            params[PoseLibActivity.DIRECTION] = getSelected(directionViews)
            Log.i("Xlong", "initView: - $params")
            onCallback.invoke(params)
            dismiss()
        }

    }

    private fun setTypeData(value: String) {
        for (view in typeViews) {
            setStatus(view, false)
        }
        when (value) {
            "0" -> setStatus(typeViews[0], true)
            "1" -> setStatus(typeViews[1], true)
            "2" -> setStatus(typeViews[2], true)
        }
    }

    private fun getSelected(views: List<TDTextView>): String {
        val indx = views.indexOfFirst { it.isActivated }
        if (indx == -1) {
            return "0"
        }
        return "$indx"
    }

    private fun setGenderData(value: String) {
        for (view in genderViews) {
            setStatus(view, false)
        }
        when (value) {
            "0" -> setStatus(genderViews[0], true)
            "1" -> setStatus(genderViews[1], true)
            "2" -> setStatus(genderViews[2], true)
        }
    }

    private fun setActionData(value: String) {
        for (view in actionViews) {
            setStatus(view, false)
        }
        when (value) {
            "0" -> setStatus(actionViews[0], true)
            "1" -> setStatus(actionViews[1], true)
            "2" -> setStatus(actionViews[2], true)
            "3" -> setStatus(actionViews[3], true)
        }
    }

    private fun setPoleData(value: String) {
        for (view in poleViews) {
            setStatus(view, false)
        }
        when (value) {
            "0" -> setStatus(poleViews[0], true)
            "1" -> setStatus(poleViews[1], true)
            "2" -> setStatus(poleViews[2], true)
            "3" -> setStatus(poleViews[3], true)
        }
    }

    private fun setHolderData(value: String) {
        for (view in holderViews) {
            setStatus(view, false)
        }
        when (value) {
            "0" -> setStatus(holderViews[0], true)
            "1" -> setStatus(holderViews[1], true)
            "2" -> setStatus(holderViews[2], true)
        }
    }

    private fun setDirectionData(value: String) {
        for (view in directionViews) {
            setStatus(view, false)
        }
        when (value) {
            "0" -> setStatus(directionViews[0], true)
            "1" -> setStatus(directionViews[1], true)
            "2" -> setStatus(directionViews[2], true)
        }
    }

    private fun setStatus(view: TDTextView, active: Boolean) {
        view.setTextColor(if (active) mActivity.resources.getColor(R.color.c_ffffff) else mActivity.resources.getColor(R.color.c_757575))
        view.setSolidAndStrokeColor(if (active) mActivity.resources.getColor(R.color.c_F82E54) else mActivity.resources.getColor(R.color.c_f2f2f2), 0)
        view.isActivated = active
    }

}