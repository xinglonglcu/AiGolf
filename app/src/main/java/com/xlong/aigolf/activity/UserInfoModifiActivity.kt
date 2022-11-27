package com.xlong.aigolf.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xlong.aigolf.BaseActivity
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.IntentUtils
import com.xlong.aigolf.utils.ScreenUtils
import com.xlong.libui.TDTextView
import com.xlong.libui.imagload.ImageLoader
import kotlinx.android.synthetic.main.activity_userinfo_modify.*

/**
 * 更改用户信息
 * Create by xlong 2022/11/23
 */
class UserInfoModifiActivity : BaseActivity() {

    private val PERMISSION_REQUEST_CODE = 0x0001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinfo_modify)
        ScreenUtils.setStatusBarColor(this, R.color.c_181818)
        initView()

        setViewEnable(tv_man, true)
        setViewEnable(tv_soccer, true)
    }

    private fun initView() {
        iv_exit.setOnClickListener {
            finish()
        }
        layout_avatar.setOnClickListener {
            checkPermissionAndLoadImages()
        }
        tv_commit.setOnClickListener {
            if (verifyInput()) {
                Toast.makeText(this, "提交", Toast.LENGTH_SHORT).show()
            }
        }

        tv_man.setOnClickListener {
            setViewEnable(tv_man, true)
            setViewEnable(tv_woman, false)
        }
        tv_woman.setOnClickListener {
            setViewEnable(tv_woman, true)
            setViewEnable(tv_man, false)
        }
        tv_soccer.setOnClickListener {
            setViewEnable(tv_soccer, true)
            setViewEnable(tv_amateur, false)
            setViewEnable(tv_coach, false)
        }
        tv_amateur.setOnClickListener {
            setViewEnable(tv_soccer, false)
            setViewEnable(tv_amateur, true)
            setViewEnable(tv_coach, false)
        }
        tv_coach.setOnClickListener {
            setViewEnable(tv_soccer, false)
            setViewEnable(tv_amateur, false)
            setViewEnable(tv_coach, true)
        }
    }

    private fun verifyInput(): Boolean {
        if (edt_nickname.text.toString().isEmpty()) {
            Toast.makeText(this, "${resources.getString(R.string.info_input_nickname)}", Toast.LENGTH_SHORT).show()
            return false
        }
        if (edt_age.text.toString().isEmpty()) {
            Toast.makeText(this, "${resources.getString(R.string.info_input_age)}", Toast.LENGTH_SHORT).show()
            return false
        }
        if (edt_des.text.toString().isEmpty()) {
            Toast.makeText(this, "${resources.getString(R.string.info_input_info)}", Toast.LENGTH_SHORT).show()
            return false
        }
        Log.d(TAG, "verifyInput: -- man:${tv_man.isActivated}; woman:${tv_woman.isActivated}; amateur:${tv_amateur.isActivated}; soccer:${tv_soccer.isActivated}; coach:${tv_coach.isActivated}")

        return true
    }

    private fun setViewEnable(textView: TDTextView, enable: Boolean) {
        if (enable) {
            textView.setSolidAndStrokeColor(resources.getColor(R.color.c_F82E54), 0)
            textView.setTextColor(resources.getColor(R.color.c_ffffff))
        } else {
            textView.setSolidAndStrokeColor(resources.getColor(R.color.c_2A2A2A), 0)
            textView.setTextColor(resources.getColor(R.color.c_ACACAC))
        }
        textView.isActivated = enable
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private fun checkPermissionAndLoadImages() {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return
        }
        val hasPermission = ContextCompat.checkSelfPermission(application, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，加载图片。
            IntentUtils.startSystemAlbumActivity(this)
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，加载图片。
                IntentUtils.startSystemAlbumActivity(this)
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "请允许存储权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentUtils.INTENT_IMAGE_MEDIA && data != null) {
            val uri = data.data
            if (uri != null) {
                try {
                    var path: String? = ""
                    if (!TextUtils.isEmpty(uri.authority)) {
                        val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null) ?: return
                        cursor.moveToFirst()
                        val indx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                        if (indx != -1) {
                            path = cursor.getString(indx)
                        }
                        cursor.close()
                    } else {
                        path = uri.path
                    }
                    Log.d(TAG, "gallery photo is $path")
                    Log.d(TAG, "gallery uri   is $uri")
                    IntentUtils.startCropImageActivity(this, uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == IntentUtils.INTENT_CROP_IMAGE && data != null) {
            val uri = data.data
            if (uri != null) {
                Log.d(TAG, "onActivityResult: INTENT_CROP_IMAGE - $uri")
                try {
                    var path: String? = ""
                    if (!TextUtils.isEmpty(uri.authority)) {
                        val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null) ?: return
                        cursor.moveToFirst()
                        val indx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                        if (indx != -1) {
                            path = cursor.getString(indx)
                        }
                        cursor.close()
                    } else {
                        path = uri.path
                    }
                    Log.d(TAG, "gallery crop photo is $path")
                    Log.d(TAG, "gallery crop uri   is $uri")
                    if (!path.isNullOrEmpty()) {
                        updatePicture(path)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updatePicture(path: String) {
        ImageLoader.load(this, path).into(iv_avatar)
        layout_add_avatar.visibility = View.GONE
    }

}