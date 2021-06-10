package com.wayapaychat.domain.models.payment

data class BankAccountDomainModel(
    val accountName: String,
    val accountNumber: String,
    val bankName: String,
    val bankCode: String
)