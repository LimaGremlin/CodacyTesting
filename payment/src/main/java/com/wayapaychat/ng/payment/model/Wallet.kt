package com.wayapaychat.ng.payment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wallet(
    val id: Int,
    val accountNo: String,
    val accountName: String,
    val balance: Int,
    val accountType: String,
    var default: Boolean
): Parcelable
