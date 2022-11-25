package com.xlong.mvi.action

import androidx.annotation.UiThread
import androidx.collection.ArrayMap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * RXJava Deduper, this is used to prevent the same action execute at the same time
 */
class RxActionDeDuper(lifecycleOwner: LifecycleOwner? = null) : ActionDeDuper {
    private val actionMap = ArrayMap<String, RxAction<*, *>>()

    init {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                disposeAll()
            }
        })
    }

    @UiThread
    override fun addAction(action: Action<*, *>) {
        // Ignore empty token
        if (action.token.isNullOrEmpty()) return
        if (action !is RxAction) {
            // Only process rxaction
            return
        }
        actionMap[action.token] = action
    }

    @UiThread
    override fun hasAction(action: Action<*, *>): Boolean = actionMap[action.token] != null

    @UiThread
    override fun queryAction(token: String): RxAction<*, *>? = actionMap[token]

    @UiThread
    override fun removeAction(action: Action<*, *>) {
        actionMap.remove(action.token)
    }

    @UiThread
    fun disposeAll() {
        actionMap.values
            .filter { it.disposable != null && it.disposable?.isDisposed == false }
            .forEach {
                it.disposable?.dispose()
            }
    }

}