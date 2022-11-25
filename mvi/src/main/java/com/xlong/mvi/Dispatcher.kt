package com.xlong.mvi

import androidx.annotation.MainThread
import com.xlong.mvi.action.Action
import com.xlong.mvi.store.BaseStore
import com.xlong.mvi.utils.ArchUtil

/**
 *
 */
typealias Dispatcher = (Action<*, *>) -> Unit

internal object GlobalDispatcher : Dispatcher {
    @MainThread
    override fun invoke(action: Action<*, *>) {
        ArchUtil.assertNotMainThread()
        stores.forEach {
            it(action)
        }
    }

    private val stores = mutableListOf<BaseStore>()

    internal fun register(store: BaseStore) {
        stores.add(store)
    }

}