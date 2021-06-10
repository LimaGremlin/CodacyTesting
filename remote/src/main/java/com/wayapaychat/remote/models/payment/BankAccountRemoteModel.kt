package com.wayapaychat.remote.models.payment

data class BankAccountRemoteModel(
    val accountName: String,
    val accountNumber: String,
    val bankName: String,
    val bankCode: String
)