package com.leeawilson.composetesting

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

data class Time(
    var hour: Int,
    var minute: Int,
    var second: Int
)

fun getTime(): Time {
    val timeInstance = Calendar.getInstance().time
    val formatter = SimpleDateFormat("hh:mm:ss", Locale.UK)
    val time = formatter.format(timeInstance)
    val instance = Time(
        hour = time.substring(0, 2).toInt(),
        minute = time.substring(3, 5).toInt(),
        second = time.substring(6, 8).toInt(),
    )
    Log.d("TimeGetter", "Time: ${instance.hour}:${instance.minute}:${instance.second}")
    return instance
}