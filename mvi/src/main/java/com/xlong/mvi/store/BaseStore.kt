package com.xlong.mvi.store

import androidx.annotation.MainThread
import android.util.SparseArray
import com.xlong.mvi.Dispatcher
import com.xlong.mvi.action.Action

abstract class BaseStore internal constructor() : Dispatcher {
    protected val reducers = SparseArray<Reducer<*, *>>()

    protected fun subscribeAction(vararg actionTypes: Pair<Int, Reducer<*, *>>) {
        actionTypes.forEach {
            reducers.put(it.first, it.second)
        }
    }

    internal fun internalCreate() {
        onCreate()
    }

    protected open fun onCreate() {
    }

    @MainThread
    override fun invoke(action: Action<*, *>) {
        onAction(action)
    }

    abstract fun onAction(action: Action<*, *>)

    abstract fun onActionResult(action: Action<*, *>, result: Any)
}
