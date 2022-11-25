package com.xlong.mvi.action

import com.xlong.mvi.Dispatcher
import com.xlong.mvi.GlobalDispatcher
import java.security.InvalidParameterException

/**
 * The instant action is an passby action. An InstantAction would be dispatched  we can use this action to send events.
 */
class InstantAction<M>(type: Int, metadata: M?, dispatcher: Dispatcher?) : Action<M, M>(
    type = type,
    metadata = metadata,
    dispatcher = dispatcher
) {
    override fun execute(): Action<M, M> {
        dispatcher?.invoke(this)
        return this
    }
}

@Suppress("UNCHECKED_CAST")
class InstantActionBuilder<M> {
    var type: Int = -1
    var metadata: M? = null
    var dispatcher: Dispatcher? = GlobalDispatcher
}

fun <M> instantAction(init: InstantActionBuilder<M>.() -> Unit): InstantAction<M> {
    val builder = InstantActionBuilder<M>()
    builder.init()
    if (builder.type == -1 && builder.dispatcher is GlobalDispatcher) {
        throw InvalidParameterException("type must be lager than 0 if this is a global action")
    }

    return InstantAction(
        type = builder.type,
        metadata = builder.metadata,
        dispatcher = builder.dispatcher
    )
}
