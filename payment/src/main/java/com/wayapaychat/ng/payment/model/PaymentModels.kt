package com.wayapaychat.ng.payment.model

import java.io.Serializable

data class Account(
    val accountName: String = "",
    val accountNumber: String = "",
    val password: String = "",
    val bankName: String = ""
):Serializable

data class CardDetail(
    val name:String = "",
    val number: String = "00000000000",
    val exMonth: String = "0",
    val exYear: String = "2021",
    val cvv: String = "69"
):Serializable

data class SuccessMessage(
    val primaryMessage: String = "",
    val secondaryMessage1: String = ""
)