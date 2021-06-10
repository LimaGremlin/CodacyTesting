package com.wayapaychat.ng.payment.model


import com.google.gson.annotations.SerializedName

data class AddCardRequest(
    var cardNumber: String = "",
    var cvv: String = "",
    var email: String = "",
    var expiryMonth: String = "",
    var expiryYear: String = "",
    var last4digit: String = "",
    var name: String = "",
    var pin: String = "",
    var userId: String = "",
    var walletAccountNumber: String = ""
)