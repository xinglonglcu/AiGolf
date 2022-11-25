package com.xlong.mvi.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * This is a simple ViewHolder extends the [RecyclerView.ViewHolder]. But this ViewHolder can dispatch
 * unbind callback when it was recycled from the adapter.
 */
abstract class UnbindableVH<T> protected constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val disposables = CompositeDisposable()
    protected val context = itemView.context
    internal var adapter: ReactiveAdapter<T>? = null
    val currentPosition: Int
        get() = adapterPosition - (adapter?.headerCount ?: 0)
    val itemCount: Int
        get() = (adapter?.dataCount ?: 0)

    protected constructor(parent: ViewGroup, @LayoutRes layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {}

    fun bind(data: T) {
        unbind()
        onBind(data)
    }

    fun unbind() {
        disposables.clear()
        onUnbind()
    }

    protected fun Disposable.autoDispose() {
        addDeposit(this)
    }

    protected fun addDeposit(disposable: Disposable) {
        disposables.add(disposable)
    }

    /**
     * Callback for bind
     */
    protected abstract fun onBind(data: T)

    /**
     * Callback for unbind
     */
    protected open fun onUnbind() {}
}
