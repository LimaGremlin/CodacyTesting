package com.wayapaychat.core.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by edetebenezer on 2019-09-02
 **/
fun String.decodeBase64IntoBitmap(): Bitmap {
    val imageBytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(
        imageBytes, 0, imageBytes.size
    )
}

fun Bitmap.encodeBitmapIntoBase64(compressFormat: Bitmap.CompressFormat, quality: Int = 0): String {
    val baseArrayOutputStream = ByteArrayOutputStream()
    compress(compressFormat, quality, baseArrayOutputStream)
    val imageBytes = baseArrayOutputStream.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}

fun String.getValueOrEmptyString(): String {
    return if (this.isNotEmpty()) this else ""
}

fun List<String>.isAnyEmpty(): Boolean {
    val empties = this.filter { it.isEmpty() }
    return empties.isEmpty()
}

/*
* compressFormat - Bitmap.CompressFormat.JPEG, Bitmap.CompressFormat.PNG, Bitmap.CompressFormat.WEBP
* quality - is range between 0 and 100
* */
fun Context.encodeImageIntoBase64(
    @DrawableRes resourceId: Int,
    compressFormat: Bitmap.CompressFormat,
    quality: Int = 0
): String {
    val bitmap = BitmapFactory.decodeResource(resources, resourceId)
    return bitmap.encodeBitmapIntoBase64(compressFormat, quality)
}

fun String.getLastFourDigits(): String {
    return this.substring(this.length - 4)
}

fun Uri.getFileName(context: Context): String {
    var result: String? = null
    if (this.scheme == "content") {
        val cursor = context.contentResolver.query(this, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result =
                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor!!.close()
        }
    }
    if (result == null) {
        result = this.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun String.getMonthDigits(): String {
    return when (this) {
        "January" -> "01"
        "February" -> "02"
        "Match" -> "03"
        "April" -> "04"
        "May" -> "05"
        "June" -> "06"
        "July" -> "07"
        "August" -> "08"
        "September" -> "09"
        "October" -> "10"
        "November" -> "11"
        else -> "12"
    }
}

@SuppressLint("SimpleDateFormat")
fun Date.dateToStringWithPattern(pattern: String): String = SimpleDateFormat(pattern).format(this)

@SuppressLint("SimpleDateFormat")
fun Date.toGokadaStringPattern(): String =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(this)

@SuppressLint("SimpleDateFormat")
fun String.parseToDateWithFormat(format: String): Date = SimpleDateFormat(format).parse(this)

@SuppressLint("SimpleDateFormat")
fun String.parseToGokadaDateWithFormat(): Date =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(this)

fun Date.dateToCalendar(): Calendar? {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

fun Calendar.zeroTime(): Calendar {
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
    this.set(Calendar.DST_OFFSET, 0)

    return this
}

fun JSONObject.tripleReplace(
    first_pattern: String,
    first_replacement: String,
    second_pattern: String,
    second_replacement: String,
    third_pattern: String,
    third_replacement: String
): JSONObject {
    val jsonString = this.toString().replace(first_pattern, first_replacement)
        .replace(second_pattern, second_replacement).replace(third_pattern, third_replacement)
    return JSONObject(jsonString)
}

fun Context.copyToClipboard(label: String, text: String) {
    val clipboard: ClipboardManager? =
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText(label, text)
    clipboard?.setPrimaryClip(clip)
}

fun Context.showShortToast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.showShortToast(@StringRes stringId: Int){
    Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(@StringRes stringId: Int){
    Toast.makeText(this, stringId, Toast.LENGTH_LONG).show()
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}

fun View.cloak(){
    visibility = View.INVISIBLE
}

fun Int.toNumberWithCommas(): String {
    val formatter = DecimalFormat("#,###,###")
    return formatter.format(this.toDouble())
}
