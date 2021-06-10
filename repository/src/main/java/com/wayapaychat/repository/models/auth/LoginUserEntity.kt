package com.wayapaychat.repository.models.auth

data class LoginUserEntity(
    var email: String,
    var id: Int,
    var phoneNumber: Long,
    var firstName: String,
    var lastName: String
)
