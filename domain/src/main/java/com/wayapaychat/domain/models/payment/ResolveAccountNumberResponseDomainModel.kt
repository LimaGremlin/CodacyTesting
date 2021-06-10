package com.wayapaychat.domain.models.payment

data class ResolveAccountNumberResponseDomainModel(
    val accountNumber: String,
    val accountName: String
)