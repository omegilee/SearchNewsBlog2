package com.example.searchnewsblog.support

import com.google.gson.Gson

/**
 * lsh 2023.04.06
 */
object GsonUtil {

    fun Any.toJson(): String? {
        val gson = Gson()
        return gson.toJson(this)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        val gson = Gson()
        return gson.fromJson(json, clazz)
    }
}