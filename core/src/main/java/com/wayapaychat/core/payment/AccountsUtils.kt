package com.wayapaychat.core.payment

import com.google.gson.JsonObject
import com.wayapaychat.core.utils.removeQuotes
import java.io.Serializable

data class AllBanks(
    val name: String = "",
    val code: String = ""
): Serializable

fun JsonObject.toAllBanks(): AllBanks {
    return AllBanks(
        name = this.get("name").toString().removeQuotes(),
        code = this.get("code").toString().removeQuotes()
    )
}

data class WayaAccounts(
    val accountName: String = "",
    val accountNumber: String = "",
    val bankName: String = "",
    val bankCode: String = "",
    val rubiesBankCode: String = "",
    var accNoLastDigits: String = accountNumber.takeLast(4),
    val userId: String = "",
): Serializable

fun JsonObject.toWayaAccount(): WayaAccounts {
    return WayaAccounts(
        accountName = this.get("accountName").toString().removeQuotes(),
        accountNumber = this.get("accountNumber").toString().removeQuotes(),
        bankName = this.get("bankName").toString().removeQuotes(),
        bankCode = this.get("bankCode").toString().removeQuotes(),
        rubiesBankCode = this.get("rubiesBankCode").toString().removeQuotes(),
        userId = this.get("userId").toString().removeQuotes(),
    )
}