package com.wayapaychat.domain.models.auth

data class LoginUser(
    val email: String = "",
    val phoneNumber: String = "",
    val pinCreated: Boolean = false,
    val userId: Int = -1,
    val firstName: String = "",
    val lastName: String = ""
)
