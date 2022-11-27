package com.xlong.aigolf.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.xlong.aigolf.activity.*

/**
 *
 * Create by xlong 2022/11/23
 */
object IntentUtils {

    const val INTENT_IMAGE_MEDIA = 200//选择图片
    const val INTENT_CROP_IMAGE = 201//裁剪图片

    fun startLoginActivity(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    fun startPhoneVerifyActivity(context: Context, mobile: String) {
        val intent = Intent(context, PhoneVerifyActivity::class.java)
        intent.putExtra("mobile", mobile)
        context.startActivity(intent)
    }

    fun startUserInfoModifiActivity(context: Context) {
        context.startActivity(Intent(context, UserInfoModifiActivity::class.java))
    }

    fun startMyCollectionActivity(context: Context) {
        context.startActivity(Intent(context, MyCollectionActivity::class.java))
    }

    fun startPoseLibActivity(context: Context) {
        context.startActivity(Intent(context, PoseLibActivity::class.java))
    }

    fun startSystemAlbumActivity(context: Activity) {
        try {
            val uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI
            val intent = Intent("android.intent.action.PICK", uri)
            intent.type = "image/*" //只过滤图片image/* 其他还有viedo/*   audio/*
            context.startActivityForResult(intent, INTENT_IMAGE_MEDIA)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 裁切图片
     */
    fun startCropImageActivity(activity: Activity, srcUri: Uri) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(srcUri, "image/*")
        intent.putExtra("crop", "true")

        // 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
        // 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
        // 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
        // 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
        // 会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
        //  不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足

        // aspectX aspectY 是裁剪框宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪后生成图片的宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        // return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
        // return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, srcUri)
        intent.putExtra("return-data", false)
        activity.startActivityForResult(intent, INTENT_CROP_IMAGE)
    }
}