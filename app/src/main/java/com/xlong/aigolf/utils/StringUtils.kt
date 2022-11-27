package com.xlong.aigolf.utils

/**
 *
 * Create by xlong 2022/11/25
 */
object StringUtils {
    fun convertToWan(num: Int): String {
        if (num > 9999) {
            return "${num / 10000}.${num % 10000 / 1000}w"
        }
        return "$num"
    }

    /**
     * 03:03:03
     */
    fun millsecondsToStr(ms: Int): String? {
        return millsecondsToStr(ms, true)
    }

    /**
     * 03:03:03
     */
    fun millsecondsToStr(seconds: Int, showHour: Boolean): String? {
        var seconds = seconds
        seconds = seconds / 1000
        var result = ""
        var hour = 0
        var min = 0
        var second = 0
        if (showHour) {
            hour = seconds / 3600
        }
        min = if (showHour) {
            Math.round(((seconds - hour * 3600) / 60).toFloat())
        } else {
            Math.round((seconds / 60).toFloat())
        }
        second = Math.round((seconds - hour * 3600 - min * 60).toFloat())
        result += if (hour < 10) {
            "0$hour:"
        } else {
            "$hour:"
        }

        // 当小时为0的时候去掉显示
        if (result == "00:") {
            result = ""
        }
        result += if (min < 10) {
            "0$min:"
        } else {
            "$min:"
        }
        if (second < 10) {
            result += "0$second"
        } else {
            result += second
        }
        return result
    }

    /**
     * 03时03分03秒
     */
    fun millsecondsToStr1(seconds: Int, showHour: Boolean): String? {
        var seconds = seconds
        seconds = seconds / 1000
        var result = ""
        var hour = 0
        var min = 0
        var second = 0
        if (showHour) {
            hour = seconds / 3600
        }
        min = if (showHour) {
            Math.round(((seconds - hour * 3600) / 60).toFloat())
        } else {
            Math.round((seconds / 60).toFloat())
        }
        second = Math.round((seconds - hour * 3600 - min * 60).toFloat())
        result += if (hour < 10) {
            "0" + hour + "时"
        } else {
            hour.toString() + "时"
        }

        // 当小时为0的时候去掉显示
        if (result == "00时") {
            result = ""
        }
        result += if (min < 10) {
            "0" + min + "分"
        } else {
            min.toString() + "分"
        }
        result += if (second < 10) {
            "0" + second + "秒"
        } else {
            second.toString() + "秒"
        }
        return result
    }
}