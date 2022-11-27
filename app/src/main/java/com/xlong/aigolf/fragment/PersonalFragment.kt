package com.xlong.aigolf.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xlong.aigolf.BaseFragment
import com.xlong.aigolf.R
import com.xlong.aigolf.utils.IntentUtils
import com.xlong.aigolf.utils.IntentUtils.PERMISSION_REQUEST_CODE
import com.xlong.libui.imagload.ImageLoader
import kotlinx.android.synthetic.main.fragment_personal.*

/**
 * 个人中心
 * Created by xlong on 2022/11/23
 */
class PersonalFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        layout_avatar.setOnClickListener {
            checkPermissionAndLoadImages()
        }
        iv_edt_intro.setOnClickListener { Toast.makeText(getMyActivity(), "iv_edt_intro", Toast.LENGTH_SHORT).show() }
        layout_set.setOnClickListener { Toast.makeText(getMyActivity(), "layout_set", Toast.LENGTH_SHORT).show() }
        layout_feedback.setOnClickListener { Toast.makeText(getMyActivity(), "layout_feedback", Toast.LENGTH_SHORT).show() }
        layout_business.setOnClickListener { Toast.makeText(getMyActivity(), "layout_business", Toast.LENGTH_SHORT).show() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): PersonalFragment {
            return PersonalFragment()
        }
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private fun checkPermissionAndLoadImages() {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return
        }
        val hasPermission = ContextCompat.checkSelfPermission(getMyActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，加载图片。
            IntentUtils.startSystemAlbumActivity(getMyActivity())
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(getMyActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，加载图片。
                IntentUtils.startSystemAlbumActivity(getMyActivity())
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(getMyActivity(), "请允许存储权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentUtils.INTENT_IMAGE_MEDIA && data != null) {
            val uri = data.data
            if (uri != null) {
                try {
                    var path: String? = ""
                    if (!TextUtils.isEmpty(uri.authority)) {
                        val cursor = getMyActivity().contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null) ?: return
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
                    IntentUtils.startCropImageActivity(getMyActivity(), uri)
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
                        val cursor = getMyActivity().contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null) ?: return
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

    }

    private fun updatePicture(path: String) {
        ImageLoader.load(getMyActivity(), path).into(iv_avatar)
        layout_add_avatar.visibility = View.GONE
    }
}