package com.wayapaychat.domain.models.auth

data class LoginHistoryDomainModel(
    val id: Int,
    val ip: String,
    val device: String,
    val city: String,
    val province: String,
    val country: String,
    val loginDate: String
)