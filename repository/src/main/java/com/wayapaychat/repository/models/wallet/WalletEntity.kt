package com.wayapaychat.repository.models.wallet

data class WalletEntity(
    val id: Int,
    val accountNo: String,
    val accountName: String,
    val balance: Int,
    val accountType: String,
    val default: Boolean
)