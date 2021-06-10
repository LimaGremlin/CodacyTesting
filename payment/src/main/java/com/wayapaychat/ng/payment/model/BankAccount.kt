package com.wayapaychat.ng.payment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BankAccount(
    val accountName: String,
    val accountNumber: String,
    val bankName: String,
    val bankCode: String
): Parcelable{
    fun lastFourDigitsAccountNumber(): String =
        accountNumber.substring(accountNumber.length - 4)
}