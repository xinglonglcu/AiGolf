package com.xlong.mvi.adapter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import com.xlong.mvi.data.ObservableList

import java.util.ArrayList
import java.util.HashSet

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Core Display Adapter for recyclerView. This adapter receives a [ListDelegate] and observe the [ObservableList]
 * in the [ListDelegate], update UI with the [ObservableList.ChangeType].
 */
class ReactiveAdapter<T>(private val dataList: ListDelegate<T>, lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<UnbindableVH<*>>(), LifecycleObserver {
    private val compositeDisposable = CompositeDisposable()
    private val activeVHs = HashSet<UnbindableVH<*>>()

    private val headers = ArrayList<ItemDelegate<*>>()
    private val footers = ArrayList<ItemDelegate<*>>()
    private val headerFooterMap = SparseArray<ItemDelegate<*>>()
    private var onItemClickListener: OnItemClickListener? = null
    internal var viewTypeLayoutArray = SparseIntArray()

    val headerCount: Int
        get() = headers.size

    val footerCount: Int
        get() = footers.size

    val dataCount: Int
        get() = dataList.size()

    init {
        val consumer: Consumer<ObservableList.Change<T>> = Consumer { change ->
            when (change.type) {
                ObservableList.ChangeType.ADD -> notifyItemRangeInserted(change.index + headerCount, change.elements.size)
                ObservableList.ChangeType.REMOVE -> notifyItemRangeRemoved(change.index + headerCount, change.elements.size)
                ObservableList.ChangeType.CLEAR -> notifyItemRangeRemoved(change.index + headerCount, change.elements.size)
                ObservableList.ChangeType.UPDATE -> notifyItemRangeChanged(change.index + headerCount, change.elements.size)
                ObservableList.ChangeType.RESET -> notifyDataSetChanged()
            }
        }
        lifecycleOwner.lifecycle.addObserver(this)
        deposit(dataList.observe().subscribe(consumer))
    }

    fun addHeader(header: ItemDelegate<*>) {
        addHeader(headers.size, header)
    }

    fun addHeader(posInHeaders: Int, header: ItemDelegate<*>) {
        if (!headers.contains(header)) {
            headers.add(posInHeaders, header)
            headerFooterMap.put(HEADER_VIEW_TYPE_START + posInHeaders, header)
        }
        notifyItemInserted(posInHeaders)
    }

    fun addHeaders(posInHeaders: Int, headers: List<ItemDelegate<*>>) {
        this.headers.addAll(posInHeaders, headers)
        val totalSize = this.headers.size
        var offset = 0
        for (header in headers) {
            headerFooterMap.put(HEADER_VIEW_TYPE_START + totalSize + offset, header)
            offset++
        }
        notifyItemRangeChanged(posInHeaders, headers.size)
    }

    fun removeHeader(header: ItemDelegate<*>) {
        val posInHeaders = headers.indexOf(header)
        if (posInHeaders < 0) {
            return
        }
        headers.removeAt(posInHeaders)
        headerFooterMap.remove(HEADER_VIEW_TYPE_START + posInHeaders)
        notifyItemRemoved(posInHeaders)
    }

    fun addFooter(footer: ItemDelegate<*>) {
        addFooter(footers.size, footer)
    }

    fun addFooter(posInFooters: Int, footer: ItemDelegate<*>) {
        if (!footers.contains(footer)) {
            footers.add(posInFooters, footer)
            headerFooterMap.put(FOOTER_VIEW_TYPE_START - posInFooters, footer)
        }
        notifyItemInserted(headerCount + dataCount + posInFooters)
    }

    fun removeFooter(footer: ItemDelegate<*>) {
        val posInFooters = footers.indexOf(footer)
        if (posInFooters < 0) {
            return
        }
        footers.removeAt(posInFooters)
        headerFooterMap.remove(FOOTER_VIEW_TYPE_START - posInFooters)
        notifyItemRemoved(headerCount + dataCount + posInFooters)
    }

    fun containsFooter(footer: ItemDelegate<*>): Boolean {
        return footers.contains(footer)
    }

    fun containsHeader(header: ItemDelegate<*>): Boolean {
        return headers.contains(header)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnbindableVH<*> {
        val headerFooter = headerFooterMap.get(viewType)
        if (headerFooter != null) {
            return headerFooter.onCreateVH(parent, viewTypeLayoutArray.get(viewType))
        }
        val vh = dataList.onCreateVH(parent, viewTypeLayoutArray.get(viewType))
        vh.adapter = this
        if (onItemClickListener != null) {
            vh.itemView.setOnClickListener { v -> onItemClickListener!!.onItemClick(v, vh, vh.adapterPosition) }
            vh.itemView.setOnLongClickListener { v -> onItemClickListener!!.onItemLongClick(v, vh, vh.adapterPosition) }
        }
        return vh
    }

    override fun onBindViewHolder(holder: UnbindableVH<*>, position: Int) {
        activeVHs.add(holder)

        val headerCount = headerCount
        val inHolder = holder as UnbindableVH<Any?>
        if (position < headerCount) {
            inHolder.bind(headers[position].data)
        } else {
            val dataCount = dataCount
            if (position < headerCount + dataCount) {
                inHolder.bind(dataList.getData(position - headerCount))
            } else {
                inHolder.bind(footers[position - headerCount - dataCount].data)
            }
        }
    }


    override fun onViewRecycled(holder: UnbindableVH<*>) {
        super.onViewRecycled(holder)
        activeVHs.remove(holder)
        holder.unbind()
    }

    override fun getItemViewType(position: Int): Int {
        val headerCount = headerCount
        val viewType: Int
        val layout: Int
        if (position < headerCount) {
            viewType = HEADER_VIEW_TYPE_START + position
            layout = headers[position].layoutRes
        } else {
            val dataCount = dataCount
            if (position < headerCount + dataCount) {
                viewType = dataList.getLayoutRes(position - headerCount)
                layout = viewType
            } else {
                viewType = FOOTER_VIEW_TYPE_START - (position - headerCount - dataCount)
                layout = footers[position - headerCount - dataCount].layoutRes
            }
        }
        viewTypeLayoutArray.put(viewType, layout)
        return viewType
    }

    override fun getItemCount(): Int {
        return headers.size + dataList.size() + footers.size
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unbindAll() {
        for (vh in activeVHs) {
            vh.unbind()
        }
        activeVHs.clear()
        compositeDisposable.clear()
    }

    private fun deposit(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun getItemData(currentPosition: Int): T? {
        return if (dataList.dataList.isEmpty()) null else dataList.dataList[currentPosition]
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    abstract class OnItemClickListener {
        open fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {}

        open fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
            return false
        }
    }

    companion object {
        private const val HEADER_VIEW_TYPE_START = 1
        private const val FOOTER_VIEW_TYPE_START = -1
    }
}
