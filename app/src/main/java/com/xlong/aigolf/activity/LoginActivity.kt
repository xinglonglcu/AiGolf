package com.xlong.aigolf.activity

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.IntentUtils
import com.xlong.aigolf.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录页
 * Create by xlong 2022/11/23
 */
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ScreenUtils.setStatusBarColor(this, R.color.c_181818)
        initView()
    }

    private fun initView() {
        tv_protocol.text = Html.fromHtml("<u>${resources.getString(R.string.protocol)}<u>")
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                changeBtnState(s.toString().length == 11)
            }
        })
        tv_get_code.setOnClickListener {
            if (verifyInput()) {
                IntentUtils.startPhoneVerifyActivity(this, edt.text.toString())
                finish()
            }
        }
        tv_weixin.setOnClickListener {
            Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show()
        }
        tv_protocol.setOnClickListener {
            Toast.makeText(this, "查看隐私协议", Toast.LENGTH_SHORT).show()
        }
        changeBtnState(false)
    }

    private fun changeBtnState(enable: Boolean) {
        if (enable) {
            tv_get_code.setSolidAndStrokeColor(resources.getColor(R.color.c_F82E54), 0)
            tv_get_code.setTextColor(resources.getColor(R.color.c_ffffff))
        } else {
            tv_get_code.setSolidAndStrokeColor(resources.getColor(R.color.c_e6e6e6), 0)
            tv_get_code.setTextColor(resources.getColor(R.color.c_cccccc))
        }
    }

    private fun verifyInput(): Boolean {
        val text = edt.text.toString()
        if (!text.startsWith("1")) {
            Toast.makeText(this, "输入正确手机号", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!chk_provision.isChecked) {
            Toast.makeText(this, "请勾选同意隐私协议", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}