package com.wayapaychat.ng.profile.presentation.model.profile

data class ChangePasswordRequest(
    val newPassword: String,
    val oldPassword: String,
    val otp: String
)