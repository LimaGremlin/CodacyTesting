package com.wayapaychat.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.wayapaychat.core.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

@SuppressLint("WrongConstant")
fun toastMessage(parentView: View, message:String){
    Snackbar.make(parentView, message, Snackbar.LENGTH_LONG).show()
}

fun hideKeyboard(activity: Activity){
    // Check if no view has focus:
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun String?.removeQuotes(): String{
    if(this == null) return ""
    var value = this
    if(value.startsWith("\""))value = value.drop(1)
    if(value.endsWith("\""))value = value.dropLast(1)
    return value
}