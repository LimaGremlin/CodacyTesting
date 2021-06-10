package com.wayapaychat.ng.payment.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.wayapaychat.core.utils.removeQuotes
import com.wayapaychat.domain.models.wallet.WalletDomainModel
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.model.Wallet
import java.io.Serializable

data class NavItem(
    val drawableId: Int = R.drawable.ic_person_text_color,
    val item: String = ""
): Serializable

fun getListNavItems(context: Context): List<NavItem>{
    return listOf(
        NavItem(R.drawable.ic_person_text_color, context.getString(R.string.my_account)),
        //NavItem(R.drawable.ic_discover_people_text_color, context.getString(R.string.discover_people)),
        //NavItem(R.drawable.ic_group_text_color, context.getString(R.string.groups_and_pages)),
        NavItem(R.drawable.ic_qr_code_text_color, context.getString(R.string.qr_code)),
        //NavItem(R.drawable.ic_interest_text_color, context.getString(R.string.interests)),
        NavItem(R.drawable.ic_qr_settings_text_color, context.getString(R.string.settings))
    )
}

fun getMonthUtils(context:Context):List<String>{
    return listOf(
        context.getString(R.string.select_string),
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
}

fun JsonObject.getWayaGramUser(): WayaGramUser {
    val userData = this.getAsJsonObject("user")
    return WayaGramUser(
        id = this.get("id").toString().removeQuotes(),
        avatar = this.get("avatar").toString().removeQuotes(),
        coverImage = this.get("coverImage").toString().removeQuotes() ?: "",
        username = this.get("username").toString().removeQuotes() ?: "",
        notPublic = this.get("notPublic").asBoolean,
        postCount = this.get("postCount").asInt,
        following = this.get("following").asInt,
        followers = this.get("followers").asInt,
        userId = userData.get("id").toString().removeQuotes() ?: "",
        email = userData.get("email").toString().removeQuotes() ?: "",
        firstName = userData.get("firstName").toString().removeQuotes() ?: "",
        surname =  userData.get("surname").toString().removeQuotes() ?: "",
        middleName = userData.get("middleName").toString().removeQuotes() ?: "",
        profileImage = userData.get("profileImage").toString().removeQuotes() ?: "",
        dateOfBirth =  userData.get("dateOfBirth").toString().removeQuotes() ?: "",
        gender = userData.get("gender").toString().removeQuotes() ?: "",
        district = userData.get("district").toString().removeQuotes() ?: "",
        address = userData.get("address").toString().removeQuotes() ?: "",
        phoneNumber = userData.get("phoneNumber").toString().removeQuotes() ?: "",
        authId = userData.get("userId").toString().removeQuotes() ?: "",
        city = userData.get("city").toString().removeQuotes() ?: "",
        corporate = userData.get("corporate").asBoolean,
    )
}

fun getQRCodeJPGPath(context: Context): String =
    (context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path ?: "") + "/wayaQRCode.jpg"

fun getQRCodePDFPath(context: Context): String =
    (context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path ?: "") + "/wayaQRCode.pdf"

fun generateQRCode(text: String): Bitmap? =
    try {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(
            text,
            BarcodeFormat.QR_CODE,
            200,
            200
        )
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }

fun WalletDomainModel.toPresentation(): Wallet =
    Wallet(
        id, accountNo, accountName, balance, accountType, default
    )
