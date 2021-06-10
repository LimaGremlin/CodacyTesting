package com.wayapaychat.remote.models.wallet

data class WalletRemoteModel(
    val id: Int,
    val accountNo: String,
    val accountName: String,
    val balance: Int,
    val accountType: String,
    val default: Boolean
)