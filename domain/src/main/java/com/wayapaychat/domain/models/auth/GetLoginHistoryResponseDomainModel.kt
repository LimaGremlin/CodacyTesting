package com.wayapaychat.domain.models.auth

data class GetLoginHistoryResponseDomainModel(
    val currentDeviceDetails: LoginHistoryDomainModel,
    val loginHistory: List<LoginHistoryDomainModel>
)