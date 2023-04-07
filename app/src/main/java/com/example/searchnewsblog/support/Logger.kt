package com.example.searchnewsblog.support

import android.util.Log

/**
 * lsh 2023.04.02
 */
object Logger {

    fun d(tag: String = "Sample", message: String) {
        Log.d(tag, message)
    }

    fun w(tag: String = "Sample", message: String) {
        Log.w(tag, message)
    }
}