package com.xlong.mvi.data

import java.util.ArrayList
import java.util.Collections

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.atomic.AtomicInteger

/**
 * A mutable list. Add/Update/Remove/Query operations are supported. We usually holder it in the low
 * level(ViewHolder/Store) and convert to [ObservableList] for UI displaying.
 * @param autoRecycle determining if clear the list while no subscribers observe it.
 */
class MutableObservableList<T> @JvmOverloads constructor(private val autoRecycle: Boolean = false) : ArrayList<T>(),
    ObservableList<T> {
    private val subject = PublishSubject.create<ObservableList.Change<T>>()
    @Volatile
    var subscriberCount = AtomicInteger(0)
        private set
    private var onRecycleCallback: Runnable? = null

    private fun notifyChange(type: ObservableList.ChangeType, index: Int, elements: Collection<T>) {
        val change = ObservableList.Change(type, index, elements)
        subject.onNext(change)
    }

    fun setOnRecycleCallback(runnable: Runnable) {
        this.onRecycleCallback = runnable
    }

    private fun provideObservable(): Observable<ObservableList.Change<T>> {
        return if (autoRecycle) {
            subject.doOnSubscribe { subscriberCount.incrementAndGet() }
                .doOnDispose {
                    subscriberCount.decrementAndGet()
                    if (subscriberCount.get() == 0) {
                        clear()
                        onRecycleCallback?.run()
                    }
                }
        } else {
            subject.hide()
        }
    }

    override fun add(element: T): Boolean {
        val result = super.add(element)
        if (result) {
            notifyChange(ObservableList.ChangeType.ADD, size - 1, setOf(element))
        }
        return result
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        notifyChange(ObservableList.ChangeType.ADD, index, setOf(element))
    }

    override fun set(index: Int, element: T): T {
        val result = super.set(index, element)
        notifyChange(ObservableList.ChangeType.UPDATE, index, listOf(element))
        return result
    }

    fun setWithoutNotify(index: Int, element: T) = super.set(index, element)

    fun removeAtWithoutNotify(index: Int) = super.removeAt(index)

    fun addWithoutNotify(index: Int, element: T) = super.add(index, element)

    override fun removeAt(index: Int): T {
        val result = super.removeAt(index)
        notifyChange(ObservableList.ChangeType.REMOVE, index, listOf(result))
        return result
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        val result = super.remove(element)
        if (result) {
            notifyChange(ObservableList.ChangeType.REMOVE, index, listOf(element))
        }
        return result
    }

    /**
     * Just like clear, but send [ObservableList.ChangeType.CLEAR] event
     */
    fun removeAll() {
        val cleared = Collections.unmodifiableList(ArrayList(this))
        super.clear()
        notifyChange(ObservableList.ChangeType.REMOVE, 0, cleared)
    }

    override fun clear() {
        val cleared = Collections.unmodifiableList(ArrayList(this))
        super.clear()
        notifyChange(ObservableList.ChangeType.CLEAR, 0, cleared)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = super.addAll(elements)
        if (result) {
            notifyChange(ObservableList.ChangeType.ADD, size - elements.size, Collections.unmodifiableCollection(elements))
        }
        return result
    }

    override fun addAll(index: Int, c: Collection<T>): Boolean {
        val result = super.addAll(index, c)
        if (result) {
            notifyChange(ObservableList.ChangeType.ADD, index, Collections.unmodifiableCollection(c))
        }
        return result
    }

    override fun observe(): Observable<ObservableList.Change<T>> {
        return provideObservable()
    }

    fun reset(c: Collection<T>) {
        super.clear()
        super.addAll(c)
        notifyChange(ObservableList.ChangeType.RESET, 0, Collections.unmodifiableCollection(c))
    }

    fun notifyReset() {
        notifyChange(ObservableList.ChangeType.RESET, 0, Collections.unmodifiableCollection(this))
    }
}
