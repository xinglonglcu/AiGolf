package com.xlong.aigolf.utils

import android.content.Context
import android.util.TypedValue
import com.xlong.aigolf.GlobalApplication

/**
 *
 * Create by xlong 2022/11/24
 */
object UIUtils {

    @JvmStatic
    fun dp2px(dp: Float): Int {
        return dip2px(GlobalApplication.appContext, dp)
    }

    @JvmStatic
    fun dip2px(context: Context, dipValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
            context.resources.displayMetrics).toInt()
    }

}