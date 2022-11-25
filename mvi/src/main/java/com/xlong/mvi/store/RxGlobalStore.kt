package com.xlong.mvi.store

import com.xlong.mvi.GlobalDispatcher

/**
 * RXGlobal store is used for global data sharing.
 */
open class RxGlobalStore: RxCommonStore() {
    override fun onCreate() {
        super.onCreate()
        GlobalDispatcher.register(this)
    }
}