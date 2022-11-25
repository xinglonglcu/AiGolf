package com.xlong.aigolf

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * Create by xlong 2022/11/23
 */
open class BaseActivity : AppCompatActivity() {
    protected val TAG = "ai_${javaClass.simpleName}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
}