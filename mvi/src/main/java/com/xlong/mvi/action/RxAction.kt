package com.xlong.mvi.action

import androidx.annotation.UiThread
import com.xlong.mvi.Dispatcher
import com.xlong.mvi.GlobalDispatcher
import com.xlong.mvi.action.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.security.InvalidParameterException

/**
 * RXAction is a rxjava based Action.
 */
@Suppress("UNCHECKED_CAST")
open class RxAction<M, R> internal constructor(
    type: Int,
    token: String? = null,
    metadata: M? = null,
    dispatcher: Dispatcher? = GlobalDispatcher,
    deDuper: RxActionDeDuper? = null,
    val actual: Observable<R>,
    private val subscribeScheduler: Scheduler = Schedulers.io(),
    private val observeScheduler: Scheduler = AndroidSchedulers.mainThread()
) : Action<M, R>(type, token, metadata, dispatcher, deDuper) {
    var actionAsync: ActionAsync<M> = Uninitialized()
        private set
    internal var disposable: Disposable? = null
    val isGlobalAction = dispatcher is GlobalDispatcher

    @UiThread
    override fun execute(): RxAction<M, R> {
        if (deDuper != null && !token.isNullOrEmpty() && deDuper.hasAction(this)) {
            // 这个Action已经在执行了，再发送一次老的Action状态，方便UI更新，且保证MetaData不会变化
            val oldAction = deDuper.queryAction(token)
            (oldAction as? RxAction<*, *>)?.dispatch()
            return this
        }
        disposable = actual.subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribe({
                actionAsync = Success(metadata = metadata, data = it)
                dispatch()
            }, { e ->
                actionAsync = Fail(metadata = metadata, e = e)
                dispatch()
            })
        deDuper?.addAction(this)
        actionAsync = Loading(metadata = metadata)
        dispatch()
        return this
    }

    fun cancel() {
        if (actionAsync is Loading) {
            disposable?.dispose()
            actionAsync = Fail(metadata = metadata, e = CancelException())
            dispatch()
        }
    }

    protected fun dispatch() {
        if (actionAsync is Success<*, *> || actionAsync is Fail) {
            deDuper?.removeAction(this)
        }
        dispatcher?.invoke(this)
    }

    companion object {
        internal val globalActionDeDuper = RxActionDeDuper()
    }
}

@Suppress("UNCHECKED_CAST")
class RxActionBuilder<M, R> {
    var type: Int = -1
    var observable: Observable<R>? = null
    var single: Single<R>? = null
    var token: String? = null
    var metadata: M? = null
    var dispatcher: Dispatcher? = GlobalDispatcher
    var deDuper: RxActionDeDuper? = null
    var subscribeScheduler: Scheduler = Schedulers.io()
    var observeScheduler: Scheduler = AndroidSchedulers.mainThread()
}

fun <M, R> typedRxAction(init: RxActionBuilder<M, R>.() -> Unit): RxAction<M, R> {
    val builder = RxActionBuilder<M, R>()
    builder.init()
    if (builder.observable == null && builder.single == null) {
        throw InvalidParameterException("observable and single can't be both null!")
    }
    if (builder.type == -1 && builder.dispatcher is GlobalDispatcher) {
        throw InvalidParameterException("type must be lager than 0 if this is a global action")
    }
    if (builder.dispatcher is GlobalDispatcher) {
        // 全局分发需要全局设置去重器
        builder.deDuper = RxAction.globalActionDeDuper
    }

    return RxAction(
        type = builder.type,
        token = builder.token,
        metadata = builder.metadata,
        dispatcher = builder.dispatcher,
        deDuper = builder.deDuper,
        actual = builder.observable ?: (builder.single?.toObservable()!!),
        subscribeScheduler = builder.subscribeScheduler,
        observeScheduler = builder.observeScheduler
    )
}

fun <R> rxAction(init: RxActionBuilder<Any?, R>.() -> Unit): RxAction<Any?, R> {
    return typedRxAction(init)
}
