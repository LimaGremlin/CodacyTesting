package com.wayapaychat.domain.models.payment

import javax.inject.Named


data class UserBankCardDetails(

    var accountName: String ,
    var authCode: Any? ,
    var cardNumber: String ,
    var email: Any? ,
    var expiryMonth: String,
    var expiryYear: String ,
    var last4digit: String ,
    var reference: Any? ,
    var type: String ,
    var userId: String ,
    var walletAccountNumber: Any? ,
    var walletFunded: Boolean

)
