package com.wayapaychat.ng.auth.presentation.model

import java.io.Serializable

data class UserSignUpDetails(
    var userId: Int = 0,
    val isCorporate: Boolean = false,
    val pinCreated: Boolean = false,
    val token: String = "",
    val firstName:String = "",
    val lastName:String = "",
    val email:String = "",
    val phoneNumber:String = "",
    val referenceCode: String = ""
):Serializable

data class Pages (
    val image: Int,
    val  title: String,
    val description: String
):Serializable

data class SaveLoginHistoryRequest(
    val city: String,
    val country: String,
    val device: String,
    val ip: String,
    val province: String
)