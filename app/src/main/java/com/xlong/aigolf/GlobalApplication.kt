package com.xlong.aigolf

import android.app.Application
import android.content.Context
import android.util.Log

/**
 *
 * Create by xlong 2022/11/24
 */
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("GlobalApplication", "onCreate: -- ${System.currentTimeMillis()}")
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
    }
}