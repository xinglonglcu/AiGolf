package com.xlong.mvi.action


/**
 * Action desuper definition
 */
interface ActionDeDuper {
    fun addAction(action: Action<*, *>)

    fun hasAction(action: Action<*, *>): Boolean

    fun queryAction(token: String): Action<*, *>?

    fun removeAction(action: Action<*, *>)
}