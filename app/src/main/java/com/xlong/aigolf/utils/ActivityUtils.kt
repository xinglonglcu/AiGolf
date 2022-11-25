package com.xlong.aigolf.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View

object ActivityUtils {
    /**
     * Return the activity by view.
     *
     * @param view The view.
     * @return the activity by view.
     */
    @JvmStatic
    fun getActivityByView(view: View): Activity? {
        return getActivityByContext(view.context)
    }

    /**
     * Return the activity by context.
     *
     * @param context The context.
     * @return the activity by context.
     */
    @JvmStatic
    fun getActivityByContext(context: Context): Activity? {
        var context = context
        if (context is Activity) return context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    /**
     * Return whether the activity is alive.
     *
     * @param context The context.
     * @return `true`: yes<br></br>`false`: no
     */
    @JvmStatic
    fun isContextAlive(context: Context): Boolean {
        return isActivityAlive(getActivityByContext(context))
    }

    /**
     * Return whether the activity is alive.
     *
     * @param activity The activity.
     * @return `true`: yes<br></br>`false`: no
     */
    @JvmStatic
    fun isActivityAlive(activity: Activity?): Boolean {
        return (activity != null && !activity.isFinishing
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed))
    }

}