package com.xlong.mvi.utils

import android.os.Looper

internal object ArchUtil {

    fun assertNotMainThread() {
        check(isMainThread()) { "Can't call this method in main thread." }
    }

    private fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }
}