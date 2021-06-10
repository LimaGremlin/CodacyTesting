package com.wayapaychat.remote.servicesutils

import okhttp3.RequestBody
import java.io.Serializable

data class WayaDonation(
    var ownerId: String = "",
    var title: String = "",
    var organizerName: String = "",
    var description: String = "",
    var tags: List<String> = mutableListOf(),
    var target: Double = 0.0,
    var displayTotalDonation: Boolean = false,
    var tag: String = "",
    var imageUrl: String = "",
    var totalDonations: Long = 0,
    var filePath: String = "",
):Serializable

fun WayaDonation.toMapRequestBody():HashMap<String, RequestBody>{

    val profileId = RequestBody.create(okhttp3.MultipartBody.FORM, this.ownerId)
    val title = RequestBody.create(okhttp3.MultipartBody.FORM, this.title)
    val description = RequestBody.create(okhttp3.MultipartBody.FORM, this.description)
    val estimatedAmount = RequestBody.create(okhttp3.MultipartBody.FORM, this.target.toString())
    val displayTotalDonation = RequestBody.create(okhttp3.MultipartBody.FORM, this.displayTotalDonation.toString())

    return hashMapOf(
        "profileId" to profileId,
        "title" to title,
        "description" to description,
        "estimatedAmount" to estimatedAmount,
        "displayTotalDonation" to displayTotalDonation,
    )
}

