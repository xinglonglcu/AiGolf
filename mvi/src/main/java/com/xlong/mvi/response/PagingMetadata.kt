package com.xlong.mvi.response

/**
 * Paging metadata for [com.tangdou.android.arch.action.ActionAsync] and [LoadingState]. Any paging
 * can be complete very simply with this metadata
 */
class PagingMetadata<T>(
        val data: T?,
        val page: Int,
        var size: Int = -1, // Maybe changed by response
        val isPullRefresh: Boolean = false
)