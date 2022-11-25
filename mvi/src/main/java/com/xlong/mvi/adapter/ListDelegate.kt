package com.xlong.mvi.adapter

import androidx.annotation.LayoutRes
import android.view.ViewGroup
import com.xlong.mvi.data.ObservableList
import io.reactivex.Observable

/**
 * ListDelegate receive a ObservableList and display it for RecyclerView items
 */
abstract class ListDelegate<T>(internal val dataList: ObservableList<T>) {
    fun observe(): Observable<ObservableList.Change<T>> {
        return dataList.observe()
    }

    fun size(): Int {
        return dataList.size
    }

    fun getData(position: Int): T {
        return dataList[position]
    }

    abstract fun onCreateVH(parent: ViewGroup, @LayoutRes layoutRes: Int): UnbindableVH<T>

    @LayoutRes
    abstract fun getLayoutRes(position: Int): Int
}
