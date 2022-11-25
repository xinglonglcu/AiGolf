package com.xlong.data.model

import android.content.Context
import java.io.File
import java.io.Serializable

/**
 *
 * Create by xlong 2021/10/8
 */
data class LutFilterModel(val id: String = "", val name: String = "",//滤镜名称
                          val thumbnail: String? = null,//小图
                          val png: String? = null, //图片资源
                          val default: Float = 0.5f, var value: Float = 0.5f, var isSelect: Boolean = false) : Serializable {
    fun getSrcPath(context: Context): String? {
        return if (!png.isNullOrEmpty() && png.lastIndexOf("/") != -1) {
            "${getFilterDir(context)}${png?.substring(png.lastIndexOf("/"), png.length)}"
        } else {
            null
        }
    }

    fun getThumbnailPath(context: Context): String? {
        return if (!thumbnail.isNullOrEmpty() && thumbnail.lastIndexOf("/") != -1) {
            "${getFilterDir(context)}${thumbnail?.substring(thumbnail.lastIndexOf("/"), thumbnail.length)}"
        } else {
            null
        }
    }

    private fun getFilterDir(context: Context): String {
        val filterDir = File("${context.externalCacheDir}/filter")
        if (!filterDir.exists()) {
            filterDir.mkdirs()
        }
        return filterDir.absolutePath
    }
}