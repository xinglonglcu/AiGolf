package com.xlong.mvi.action

import com.xlong.mvi.Dispatcher

sealed class ActionAsync<out M>(val metadata: M? = null)

class Uninitialized<out M> : ActionAsync<M>()
class Loading<out M>(metadata: M?) : ActionAsync<M>(metadata)
class Success<out M, out R>(metadata: M? = null, val data: R) : ActionAsync<M>(metadata)
class Fail<out M>(metadata: M? = null, val e: Throwable) : ActionAsync<M>(metadata)


abstract class Action<M, R>(
    // Action type
    val type: Int,
    // Action identifier for deDuper
    val token: String? = null,
    // Action metadata
    val metadata: M? = null,
    // Where the action dispatch to
    val dispatcher: Dispatcher? = null,
    // Make sure the same action can't be executed at the same time
    val deDuper: ActionDeDuper? = null
) {
    abstract fun execute(): Action<M, R>
}
