package com.wayapaychat.remote.models.auth

data class LoginHistoryRemoteModel(
    val id: Int,
    val ip: String,
    val device: String,
    val city: String,
    val province: String,
    val country: String,
    val loginDate: String
)