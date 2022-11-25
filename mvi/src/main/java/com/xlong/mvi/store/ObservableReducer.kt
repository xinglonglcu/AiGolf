package com.xlong.mvi.store

import com.xlong.mvi.action.Action
import com.xlong.mvi.action.RxAction
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

typealias RxReducerOp<T, R> = (action: RxAction<*, T>) -> R

/**
 * An observable reducer for RXJava.
 */
open class ObservableReducer<T, R>(
    val isBehavior: Boolean = false,
    private val reducerOp: RxReducerOp<T, R>
) : Reducer<T, R> {
    override fun invoke(action: Action<*, *>) {
        val result = reduce(action as Action<*, T>)
        subject.onNext(result)
    }

    override fun reduce(action: Action<*, T>): R {
        check(action is RxAction) { "ObservableReducer can only process RXAction" }
        return reducerOp(action)
    }

    internal val subject: Subject<R> =
        if (isBehavior) BehaviorSubject.create() else PublishSubject.create()

    val cache: R?
        get() = (subject as? BehaviorSubject)?.value

    val observable: Observable<R> get() = subject.hide()
}