package com.xlong.mvi.vm

import androidx.lifecycle.ViewModel
import com.xlong.mvi.action.RxAction
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Extends from Google's official ViewModel
 */
abstract class RxViewModel : ViewModel() {
    private val disposes = CompositeDisposable()
    protected var isCleared = false
        private set

    fun Disposable.autoDispose() {
        disposes.add(this)
    }

    /**
     * observe with Observable data and auto dispose when ViewModel cleared
     */
    fun <T> Observable<T>.observe(consumer: Consumer<in T>) {
        disposes.add(this.subscribe(consumer))
    }

    /**
     * observe with Single data and auto dispose when ViewModel cleared
     */
    fun <T> Single<T>.observe(consumer: Consumer<in T>) {
        disposes.add(this.subscribe(consumer))
    }

    override fun onCleared() {
        isCleared = true
        disposes.clear()
    }

    /**
     * make sure action will not run after ViewModel is cleared
     */
    fun RxAction<*, *>.executeInVM(): RxAction<*, *> {
        if (isCleared) return this
        return execute()
    }
}