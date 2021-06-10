package com.wayapaychat.remote.utils

fun Boolean?.safeBoolean(): Boolean = this ?: false

fun Float?.safeFloat(): Float = this ?: 0f

fun Long?.safeLong(): Long = this ?: 0L

fun String?.safeString(): String = this ?: "N/A"

fun Int?.safeInt(): Int = this ?: 0

fun Double?.safeDouble(): Double = this ?: 0.00
