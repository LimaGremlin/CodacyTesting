package com.wayapaychat.core.models

import java.io.Serializable

data class TempWallet(
    val id: Int = -1,
    val accountNo: String = "",
    val accountName: String = "",
    val balance: Double = 0.00,
    val accountType: String = "",
    var default: Boolean = false
): Serializable