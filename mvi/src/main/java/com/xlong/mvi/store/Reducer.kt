package com.xlong.mvi.store

import com.xlong.mvi.Dispatcher
import com.xlong.mvi.action.Action

//typealias Reducer<T, R> = (action: Action<*, T>) -> R

/**
 * A reducer is also a dispatcher. Action could dispatch to a reducer directly.
 */
interface Reducer<T, R> : Dispatcher {
    override fun invoke(p1: Action<*, *>) {
        // Do nothing
    }

    fun reduce(action: Action<*, T>): R
}
