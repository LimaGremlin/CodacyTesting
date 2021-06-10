package com.wayapaychat.repository.models.auth

data class LoginHistoryEntity(
    val id: Int,
    val ip: String,
    val device: String,
    val city: String,
    val province: String,
    val country: String,
    val loginDate: String
)