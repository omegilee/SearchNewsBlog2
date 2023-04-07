package com.example.searchnewsblog.data.database


import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimestampConverter {

    @SuppressLint("SimpleDateFormat")
    private val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun fromTimestamp(value: String?): Date? {
        return if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            null
        } else {
            null
        }
    }

    fun fromTimestamp(value: Long): String {
        return value.let {
            df.format(Date(it))
        }
    }
}