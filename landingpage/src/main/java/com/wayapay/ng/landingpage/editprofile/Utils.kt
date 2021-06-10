package com.wayapay.ng.landingpage.editprofile

import com.wayapay.ng.landingpage.models.WayaGroup
import okhttp3.RequestBody
import java.io.Serializable

data class EditGramProfile(
    var headerImgPath: String = "",
    var profileImgPath: String = "",
    var username: String = "",
    var bio: String = "",
    var isPrivate: Boolean = false,
):Serializable

fun EditGramProfile.toMapRequestBody(userId: String):HashMap<String, RequestBody>{

    val userId = RequestBody.create(okhttp3.MultipartBody.FORM, userId)
    val username = RequestBody.create(okhttp3.MultipartBody.FORM, this.username)
    val notPublic = RequestBody.create(okhttp3.MultipartBody.FORM, this.isPrivate.toString())

    return hashMapOf(
        "user_id" to userId,
        "username" to username,
        "notPublic" to notPublic,
    )
}