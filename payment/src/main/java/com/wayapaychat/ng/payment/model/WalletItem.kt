package com.wayapaychat.ng.payment.model

data class WalletItem (
        val no: Int?,
        val id: String,
        val accountNumber: String,
        val balance: Int? = 0,
        val default: Boolean? = false
)
