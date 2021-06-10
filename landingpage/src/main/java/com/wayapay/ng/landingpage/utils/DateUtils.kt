package com.wayapay.ng.landingpage.utils

import java.util.*

fun addDays(time: Long, days:Int):Long{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    calendar.add(Calendar.DAY_OF_MONTH, days)

    return calendar.timeInMillis
}