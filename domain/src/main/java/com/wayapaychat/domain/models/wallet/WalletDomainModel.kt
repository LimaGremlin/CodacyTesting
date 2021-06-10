package com.wayapaychat.domain.models.wallet

data class WalletDomainModel(
    val id: Int,
    val accountNo: String,
    val accountName: String,
    val balance: Int,
    val accountType: String,
    val default: Boolean
)