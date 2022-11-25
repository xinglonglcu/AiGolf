package com.xlong.mvi.store

import com.xlong.mvi.action.Action
import com.xlong.mvi.action.RxAction

/**
 * RXJava based store, use subscribeAction to subscribe an action
 */
abstract class RxCommonStore : BaseStore() {
    /**
     * Receive action here
     */
    final override fun onAction(action: Action<*, *>) {
        if (action is RxAction)
            onRxAction(action)
        else {
            val reducer = reducers[action.type] ?: return
            val forceReducer = reducer as Reducer<Any, Any>
            val forceAction = action as Action<Any, Any>
            val result = forceReducer.reduce(forceAction)
            onActionResult(action, result)
        }
    }

    /**
     * Process RxAction
     */
    @SuppressWarnings("unchecked")
    fun onRxAction(action: RxAction<*, *>) {
        val reducer = reducers[action.type]
        val forceReducer = reducer as? Reducer<Any?, Any>
        val forceAction = action as RxAction<Any?, Any?>
        val result = forceReducer?.reduce(forceAction)
        result?.let {
            if (forceReducer is ObservableReducer<Any?, Any>) {
                forceReducer.subject.onNext(result)
            }
            onActionResult(action, result)
        }
    }

    /**
     * You can implement this function if you subscribed an action that is not RxAction
     */
    override fun onActionResult(action: Action<*, *>, result: Any) {
        // Ignore in RxStore
    }
}