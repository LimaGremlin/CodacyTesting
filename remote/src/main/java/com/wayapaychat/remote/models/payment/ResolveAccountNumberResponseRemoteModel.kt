package com.wayapaychat.remote.models.payment

import com.google.gson.annotations.SerializedName

data class ResolveAccountNumberResponseRemoteModel(
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("account_name")
    val accountName: String
)