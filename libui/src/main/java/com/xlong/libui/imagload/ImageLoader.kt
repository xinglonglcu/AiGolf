package com.xlong.libui.imagload

import android.app.Activity
import android.content.Context
import android.net.Uri
import java.io.File

/**
 * Create by xfchen
 * on 2019-10-23 10:31
 */
object ImageLoader {
    @JvmStatic
    fun load(activity: Activity, url: String): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).load(url)
    }

    @JvmStatic
    fun load(context: Context, url: String): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).load(url)
    }

    @JvmStatic
    fun load(activity: Activity, url: Uri): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).load(url)
    }

    @JvmStatic
    fun load(context: Context, url: Uri): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).load(url)
    }

    @JvmStatic
    fun loadAsGif(activity: Activity, url: String): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).loadAsGif(url)
    }

    @JvmStatic
    fun loadAsGif(context: Context, url: String): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).loadAsGif(url)
    }

    @JvmStatic
    fun loadAsBitmap(activity: Activity, url: String): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).loadAsBitmap(url)
    }

    @JvmStatic
    fun loadAsBitmap(context: Context, url: String): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).loadAsBitmap(url)
    }

    @JvmStatic
    fun loadAsBitmap(activity: Activity, source: Int): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).loadAsBitmap(source)
    }

    @JvmStatic
    fun loadAsBitmap(context: Context, source: Int): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).loadAsBitmap(source)
    }

    @JvmStatic
    fun loadAsBitmap(activity: Activity, file: File): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).loadAsBitmap(file)
    }

    @JvmStatic
    fun loadAsBitmap(context: Context, file: File): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).loadAsBitmap(file)
    }

    @JvmStatic
    fun load(activity: Activity, file: File): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).load(file)
    }

    @JvmStatic
    fun load(context: Context, file: File): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).load(file)
    }

    @JvmStatic
    fun load(activity: Activity, source: Int): ImageLoaderBuilder {
        return ImageLoaderBuilder(activity).load(source)
    }

    @JvmStatic
    fun load(context: Context, source: Int): ImageLoaderBuilder {
        return ImageLoaderBuilder(context).load(source)
    }

}
