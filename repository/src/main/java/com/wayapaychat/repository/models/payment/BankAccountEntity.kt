package com.wayapaychat.repository.models.payment

data class BankAccountEntity(
    val accountName: String,
    val accountNumber: String,
    val bankName: String,
    val bankCode: String
)