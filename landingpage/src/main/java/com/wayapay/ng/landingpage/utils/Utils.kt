package com.wayapay.ng.landingpage.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.models.*
import com.wayapaychat.local.room.models.WayaGramUser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws


data class NavItem(
    val drawableId: Int = R.drawable.ic_person_text_color,
    val item: String = ""
):Serializable

fun getSomePages(): List<WayaPages> =
        listOf(
                WayaPages(description = "Home page for home", pageName = "Fan page"),
                WayaPages(description = "Entangled Lovers", pageName = "Love page"),
                WayaPages(description = "Developers", pageName = "GDG"),
        )

fun getSomeGroups(): List<WayaGroup> =
        listOf(
                WayaGroup(description =  "Group for Manchester United fans", groupName = "Man U fan page"),
                WayaGroup(description =  "Home for Afro-Beat", groupName = "On the beat"),
                WayaGroup(description =  "If you're a party animal join the group", groupName = "Outsiders"),
                WayaGroup(description =  "Association of Nigerian Doctors", groupName = "AND"),
        )

fun getSomeUsers(): List<WayaGramUser> =
        listOf(
            WayaGramUser(displayName = "Anga Koko", username = "angakoko"),
            WayaGramUser(displayName = "Michelle Joshuah", username = "michelle"),
            WayaGramUser(displayName = "Ebenezer Edet", username = "ezer_e"),
            WayaGramUser(displayName = "Omolewa Gyem", username = "omo_gyem"),
            WayaGramUser(displayName = "Chigozie Asadu", username = "chigozieasadu"),
        )

fun getSomeInterest():List<WayaInterest> =
    listOf(
            WayaInterest("", "Sports"),
            WayaInterest("", "Call of duty"),
            WayaInterest("", "Rap"),
            WayaInterest("", "R&B"),
            WayaInterest("", "Big brother Nija"),
            WayaInterest("", "Android Development"),
            WayaInterest("", "Technology"),
            WayaInterest("", "Artificial Intelligence"),
            WayaInterest("", "NBA"),
            WayaInterest("", "Premier League"),
            WayaInterest("", "Champions League"),
            WayaInterest("", "Basket Ball"),
            WayaInterest("", "Afro Beat"),
            WayaInterest("", "Lil Wayne"),
            WayaInterest("", "Burna Boy"),
    )

fun getListNavItems(context: Context): List<NavItem>{
    return listOf(
        NavItem(R.drawable.ic_person_text_color, context.getString(R.string.my_account)),
        NavItem(R.drawable.ic_discover_people_text_color, context.getString(R.string.discover_people)),
        NavItem(R.drawable.ic_group_text_color, context.getString(R.string.groups_and_pages)),
        NavItem(R.drawable.ic_qr_code_text_color, context.getString(R.string.qr_code)),
        NavItem(R.drawable.ic_interest_text_color, context.getString(R.string.interests)),
        NavItem(R.drawable.ic_qr_settings_text_color, context.getString(R.string.settings))
    )
}

fun getLastHourOfDay(date: Calendar):Long{
    // reset hour, minutes, seconds and millis
    date[Calendar.HOUR_OF_DAY] = 23
    date[Calendar.MINUTE] = 59
    date[Calendar.SECOND] = 59
    date[Calendar.MILLISECOND] = 999

    return date.timeInMillis
}

fun getVotePercent(count: Int, totalCount: Int):String{

    if(count == 0) return "0%"
    val result = (count/totalCount) * 100
    return "$result%"
}

fun getFirstHourOfDay(date: Calendar):Long{
    // reset hour, minutes, seconds and millis
    date[Calendar.HOUR_OF_DAY] = 0
    date[Calendar.MINUTE] = 0
    date[Calendar.SECOND] = 0
    date[Calendar.MILLISECOND] = 0

    return date.timeInMillis
}

fun getTime(time:Long?):String{
    val ts = time ?: System.currentTimeMillis()
    //Get instance of calendar
    val calendar = Calendar.getInstance(Locale.getDefault())
    //get current date from ts
    calendar.timeInMillis = ts
    //return formatted date
    return android.text.format.DateFormat.format("hh:mm a", calendar).toString()
}

fun getNumberDate(time:Long?):String{
    val ts = time ?: System.currentTimeMillis()
    //Get instance of calendar
    val calendar = Calendar.getInstance(Locale.getDefault())
    //get current date from ts
    calendar.timeInMillis = ts
    //return formatted date
    return android.text.format.DateFormat.format("MM/dd/yyyy", calendar).toString()
}

fun getShortDate(time:Long? = System.currentTimeMillis()):String{
    val ts = time ?: System.currentTimeMillis()
    //Get instance of calendar
    val calendar = Calendar.getInstance(Locale.getDefault())
    //get current date from ts
    calendar.timeInMillis = ts
    //return formatted date
    return android.text.format.DateFormat.format("E, dd MMM yyyy", calendar).toString()
}

/**
 * Used to get a file path of image. Used when taking picture with the camera
 */
@Throws(IOException::class)
fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

fun getAbsolutePath(context: Context, uri: Uri): String? {
    var result: String? = null
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = context.contentResolver.query(uri, proj, null, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(proj[0])
            result = cursor.getString(columnIndex)
        }
        cursor.close()
    }
    if (result == null) {
        result = "Not found"
    }
    return result
}

fun getMultiPartBody(path: String, filePath: String):MultipartBody.Part?{
    if(TextUtils.isEmpty(filePath)) return null
    return try {
        val file = File(filePath)
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        MultipartBody.Part.createFormData(path, file.name, requestFile)
    }catch (e: Exception){
        null
    }
}

fun ImageView.setImageUri(imgUri:String){
    Glide.with(this.context.applicationContext)
        .load(imgUri.toUri())
        .transform(CenterCrop(), RoundedCorners(24))
        .placeholder(com.wayapaychat.core.R.drawable.shape_image_place_holder)
        .signature(ObjectKey(java.lang.System.currentTimeMillis()))
        .error(com.wayapaychat.core.R.drawable.shape_image_place_holder)
        .into(this)
}

fun ImageView.setProfileUri(imgUri: String){
    Glide.with(this)
        .load(imgUri.toUri())
        .placeholder(R.drawable.ic_person)
        .signature(ObjectKey(System.currentTimeMillis()))
        .apply(
            RequestOptions.circleCropTransform()
                //.placeholder(R.drawable.circle_default_background)
                .error(com.wayapaychat.core.R.drawable.ic_person)
        )
        .into(this)
}

fun userNameIsConfirmed(username:String):Boolean{
    val regex = "^(?=[a-zA-Z0-9._]{4,15}\$)(?!.*[_.]{2})[^_.].*[^.]\$".toRegex()
    return regex.matches(username)
}

fun TextView.toDouble():Double = text.toString().toDoubleOrNull() ?: 0.0