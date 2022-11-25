package com.xlong.mvi.ktx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.xlong.mvi.Dispatcher
import com.xlong.mvi.action.RxActionDeDuper
import com.xlong.mvi.action.rxAction
import com.xlong.mvi.store.BaseStore
import com.xlong.mvi.store.RxGlobalStore
import io.reactivex.Observable
import io.reactivex.Single

@RestrictTo(RestrictTo.Scope.LIBRARY)

private object UninitializedValue


class StoreCreateLazy<out T : BaseStore>(private val clazz: Class<T>) : Lazy<T> {
    private var _value: Any? = UninitializedValue
    private val lock = this

    @Suppress("UNCHECKED_CAST")
    override val value: T
        get() {
            @SuppressWarnings("Detekt.VariableNaming")
            val _v1 = _value
            if (_v1 !== UninitializedValue) {
                return _v1 as T
            }

            return synchronized(lock) {
                @SuppressWarnings("Detekt.VariableNaming")
                val _v2 = _value
                if (_v2 !== UninitializedValue) {
                    _v2 as T
                } else {
                    _value = storeInst(clazz)
                    _value as T
                }
            }
        }

    override fun isInitialized(): Boolean = _value !== UninitializedValue

    companion object {
        private val storeMap = ArrayMap<Class<out BaseStore>, BaseStore>()

        @Suppress("UNCHECKED_CAST")
        @MainThread
        @JvmStatic
        fun <T : BaseStore> storeInst(clazz: Class<T>): T {
            return if (RxGlobalStore::class.java.isAssignableFrom(clazz)) {
                (storeMap[clazz] ?: clazz.newInstance().apply {
                    storeMap[clazz] = this
                    internalCreate()
                }) as T
            } else {
                clazz.newInstance().apply {
                    internalCreate()
                }
            }
        }
    }
}

inline fun <T, reified S : BaseStore> T.lazyStore(): StoreCreateLazy<S> {
    return StoreCreateLazy(S::class.java)
}

inline fun <T, reified VM : ViewModel> T.lazyViewModel(fragment: Fragment): Lazy<VM> {
    return lazy {
        ViewModelProvider(fragment)[VM::class.java]
    }
}

inline fun <T, reified VM : ViewModel> T.lazyViewModel(activity: FragmentActivity): Lazy<VM> {
    return lazy {
        ViewModelProvider(activity)[VM::class.java]
    }
}

fun <T> Single<T>.executeAsAction(
    dispatcher: Dispatcher,
    type: Int = -1,
    metadata: Any? = null,
    token: String? = null,
    deDuper: RxActionDeDuper? = null
) {
    rxAction<T> {
        this.type = type
        this.token = token
        this.single = this@executeAsAction
        this.metadata = metadata
        this.dispatcher = dispatcher
        this.deDuper = deDuper
    }.execute()
}

fun <T> Observable<T>.executeAsAction(
    dispatcher: Dispatcher,
    type: Int = -1,
    metadata: Any? = null,
    token: String? = null,
    deDuper: RxActionDeDuper? = null
) {
    rxAction<T> {
        this.type = type
        this.token = token
        this.observable = this@executeAsAction
        this.metadata = metadata
        this.dispatcher = dispatcher
        this.deDuper = deDuper
    }.execute()
}
