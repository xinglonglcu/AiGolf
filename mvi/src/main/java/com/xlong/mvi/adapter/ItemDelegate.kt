package com.xlong.mvi.adapter

import androidx.annotation.LayoutRes
import android.view.ViewGroup

/**
 * Normal delegate for a single item. This usually use for RecyclerView headers or footers displaying
 */
abstract class ItemDelegate<T> protected constructor(val data: T) {

    @get:LayoutRes
    abstract val layoutRes: Int

    abstract fun onCreateVH(parent: ViewGroup, @LayoutRes layoutRes: Int): UnbindableVH<T>
}
