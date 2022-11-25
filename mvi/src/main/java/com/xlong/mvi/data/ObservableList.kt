package com.xlong.mvi.data

import io.reactivex.Observable

/**
 * An read-only and observable list.
 */
interface ObservableList<T> : List<T> {
    fun observe(): Observable<Change<T>>

    enum class ChangeType {
        ADD, REMOVE, CLEAR, UPDATE, RESET
    }
    class Change<T>(val type: ChangeType, val index: Int, val elements: Collection<T>)
}
