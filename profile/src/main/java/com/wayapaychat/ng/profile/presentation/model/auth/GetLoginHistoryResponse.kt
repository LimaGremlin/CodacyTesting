package com.wayapaychat.ng.profile.presentation.model.auth

data class GetLoginHistoryResponse(
    val currentDeviceDetails: LoginHistory,
    val loginHistory: List<LoginHistory>
)