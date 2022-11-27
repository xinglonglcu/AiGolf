package com.xlong.aigolf.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.ScreenUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_phone_verify.*
import java.util.concurrent.TimeUnit

/**
 * 手机验证码验证
 * Create by xlong 2022/11/23
 */
class PhoneVerifyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verify)
        ScreenUtils.setStatusBarColor(this, R.color.c_181818)
        val mobile = intent.getStringExtra("mobile")
        if (!mobile.isNullOrEmpty() && mobile.length == 11) {
            tv_phone.text = "${resources.getString(R.string.has_send)} +86 ${mobile.replaceRange(3, 7, "****")}"
        }
        initView()
        startTimer()
    }

    private fun initView() {
        tv_login.setOnClickListener {
            if (verifyInput()) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
            }
        }
        tv_back.setOnClickListener {
            finish()
        }

        tv_timing.setOnClickListener {
            Toast.makeText(this, "重新发送", Toast.LENGTH_SHORT).show()
            startTimer()
        }
        setTextWatcher(edt_1)
        setTextWatcher(edt_2)
        setTextWatcher(edt_3)
        setTextWatcher(edt_4)
        setTextWatcher(edt_5)
        setTextWatcher(edt_6)
    }

    private fun setTextWatcher(view: EditText) {
        view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) {
                    when (view.id) {
                        R.id.edt_1 -> {
                            edt_2.requestFocus()
                        }
                        R.id.edt_2 -> {
                            edt_3.requestFocus()
                        }
                        R.id.edt_3 -> {
                            edt_4.requestFocus()
                        }
                        R.id.edt_4 -> {
                            edt_5.requestFocus()
                        }
                        R.id.edt_5 -> {
                            edt_6.requestFocus()
                        }
                        R.id.edt_6 -> {
                            tv_login.requestFocus()
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun verifyInput(): Boolean {
        if (edt_1.text.toString().isEmpty() || edt_2.text.toString().isEmpty() || edt_3.text.toString().isEmpty() || edt_4.text.toString().isEmpty() || edt_5.text.toString()
                .isEmpty() || edt_6.text.toString().isEmpty()
        ) {
            Toast.makeText(this, resources.getString(R.string.input_code), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun startTimer() {
        tv_timing.isClickable = false
        tv_timing.isEnabled = false
        Observable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe {
            Log.d(TAG, "startTimer: -- $it")
            tv_timing.text = "${resources.getString(R.string.resend)}(${60 - it}s)"
            if (it == 60L) {
                tv_timing.isClickable = true
                tv_timing.isEnabled = true
                tv_timing.text = resources.getString(R.string.resend)
            }
        }
    }
}