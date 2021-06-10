package com.wayapaychat.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import com.wayapaychat.core.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object Helpers {

    fun makeClickableSpan(
        method: (() -> Unit?)? = null,
        color: Int,
        underline: Boolean,
        txt: TextView,
        from: Int,
        to: Int,
        content: String
    ) {

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                if (method != null) {
                    method()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = underline
                ds.color = txt.context.resources.getColor(R.color.colorPrimary)
            }

        }
        val ss = SpannableString(content)
        ss.setSpan(clickableSpan, from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt.text = ss
        txt.isClickable = true
        txt.movementMethod = LinkMovementMethod.getInstance()
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String.isValidPhone(): Boolean {
        return !TextUtils.isEmpty(this) &&
                android.util.Patterns.PHONE.matcher(this).matches() && this.length > 8
    }

    fun String.replaceWithCountryCode(): String {
        return "234${this.removePrefix("0")}"
    }

    fun String.replaceWithCountryCodeWithPlus(): String {
        return "+234${this.removePrefix("0")}"
    }

    fun String.isVeryEmpty(): Boolean {
        return !TextUtils.isEmpty(this) // this says the string is not empty
    }

    fun String.isShort(): Boolean {
        return this.length >= 3 // this says the string is not empty
    }

    fun String.isValidPassword(): Boolean {
        val PASSWORD_REGEX = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$")
        return this.matches(PASSWORD_REGEX)
    }

    fun View.deactivate() {
        this.alpha = 0.4f
        this.isClickable = false
        this.isActivated = false
    }

    fun View.activate() {
        this.alpha = 1f
        this.isClickable = true
        this.isActivated = true
    }

    fun TextInputLayout.makeEditable() {
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.isEnabled = true
    }

    fun TextInputLayout.makeNotEditable() {
        this.isFocusable = false
        this.isFocusableInTouchMode = false
        this.isEnabled = false
    }

    fun TextInputEditText.makeClickableAlone() {
        this.isFocusable = false
        this.isClickable = true
    }

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.reveal() {
        this.visibility = View.VISIBLE
    }

    fun ViewPager.showSideItems() {
        this.clipToPadding = false
        this.setPadding(70, 0, 70, 0)
        this.pageMargin = 50
    }

    fun ImageView.setImage(image: Int) {
        this.setImageResource(image)
    }

//    fun ImageView.loadImageFromUrl(url: String, context: Context) {
//        Glide.with(context).load(url)
//                .apply(RequestOptions()
//                        .override(200, 200)
//                        .error(R.drawable.ic_account))
//                .into(this)
//    }

    fun ImageView.loadImageFromUrl(url: String, context: Context) {
        if (!url.isNullOrEmpty()) {
            Picasso.get().load(url).into(this)
        }
    }

    fun Activity.moveToAnother(destination: Any, bundle: Bundle? = null) {
        val intent = Intent(this, destination::class.java)
        if (bundle != null) {
            intent.putExtra("data", bundle)
        }
        this.startActivity(intent)
        this.finish()
    }

    @SuppressLint("NewApi")
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses =
                am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }


    fun String.toInt(): Int = java.lang.Integer.parseInt(this)


    fun printDifference(startDate: Date, endDate: Date) {
        //milliseconds
        var different: Long = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        System.out.printf(
            "%d days, %d hours, %d minutes, %d seconds%n",
            elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
        )
    }

    fun String.getDateWithServerTimeStamp(): Date? {
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            Locale.getDefault()
        )
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
        try {
            return dateFormat.parse(this)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateDiff(oldDate: Date): String {
        var day = 0
        var hh = 0
        var mm = 0
        try {
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm aa")

            val datetime = LocalDateTime.now()
            val timeDiff = oldDate.time - oldDate.time
            day = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(timeDiff).toInt()
            hh =
                    ((java.util.concurrent.TimeUnit.MILLISECONDS.toHours(timeDiff) - java.util.concurrent.TimeUnit.DAYS.toHours(
                            day.toLong()
                    )).toInt())
            mm =
                    ((java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(timeDiff) - java.util.concurrent.TimeUnit.HOURS.toMinutes(
                            java.util.concurrent.TimeUnit.MILLISECONDS.toHours(timeDiff)
                    )).toInt())
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return if (mm <= 60 && hh != 0) {
            if (hh <= 60 && day != 0) {
                "$day DAYS AGO"
            } else {
                "$hh HOUR AGO"
            }
        } else {
            "$mm MIN AGO"
        }
    }

    fun getDeviceName(): String =
            if (MODEL.startsWith(MANUFACTURER, ignoreCase = true)) {
                MODEL
            } else {
                "$MANUFACTURER $MODEL"
            }.capitalize(Locale.ROOT)
}


