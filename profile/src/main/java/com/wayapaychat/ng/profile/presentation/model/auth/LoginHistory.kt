package com.wayapaychat.ng.profile.presentation.model.auth

import java.io.Serializable

data class LoginHistory(
    var id: Int = 0,
    val ip: String = "",
    val device: String= "",
    val city: String ="",
    val province: String ="",
    val country: String ="",
    val loginDate: String =""
    ): Serializable
