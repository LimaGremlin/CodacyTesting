package com.wayapaychat.core.payment

import com.google.gson.JsonObject
import com.wayapaychat.core.utils.removeQuotes
import java.io.Serializable

data class Wallet(
    val id: Int = -1,
    val accountNo: Int = -1,
    val productId: Int = -1,
    val productName: String = "",
    val statusId: Int = -1,
    val statusCode: String = "",
    val statusValue: String = "",
    val submittedAndPendingApproval: Boolean = false,
    val approved: Boolean = false,
    val rejected: Boolean = false,
    val withdrawnByApplication: Boolean = false,
    val active: Boolean = false,
    val closed: Boolean = false,
    val currencyCode: String = "",
    val currencyName: String = "",
    val currencyDecimalPlace: Int = -1,
    val currencyNameCode: String = "",
    val currencyDisplayLabel: String = ""
): Serializable

fun JsonObject.toWallet(): Wallet {
    val status = this.get("status").asJsonObject
    val currency = this.get("currency").asJsonObject

    return Wallet(
        id = this.get("id").asInt,
        accountNo = this.get("accountNo").asInt,
        productId = this.get("productId").asInt,
        productName = this.get("productName").toString().removeQuotes(),
        statusId = status.get("id").asInt,
        statusCode = status.get("code").toString().removeQuotes(),
        statusValue = status.get("value").toString().removeQuotes(),
        submittedAndPendingApproval = status.get("submittedAndPendingApproval").asBoolean,
        approved = status.get("approved").asBoolean,
        rejected = status.get("rejected").asBoolean,
        withdrawnByApplication = status.get("withdrawnByApplicant").asBoolean,
        active = status.get("active").asBoolean,
        closed = status.get("closed").asBoolean,
        currencyCode = currency.get("code").toString().removeQuotes(),
        currencyName = currency.get("name").toString().removeQuotes(),
        currencyDecimalPlace = currency.get("decimalPlaces").asInt,
        currencyNameCode = currency.get("nameCode").toString().removeQuotes(),
        currencyDisplayLabel = currency.get("displayLabel").toString().removeQuotes()
    )
}