package com.wayapaychat.core.newtwork

import com.google.gson.JsonObject
import com.wayapaychat.core.models.LogInInfo
import com.wayapaychat.core.models.TempWallet
import com.wayapaychat.core.utils.removeQuotes
import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.domain.models.payment.UserBankCardDetails
import com.wayapaychat.local.room.models.WayaGramUser

fun JsonObject.getWayaGramUser(): WayaGramUser {
    val userData = this.getAsJsonObject("user")
    return WayaGramUser(
            id = this.get("id").toString().removeQuotes(),
            avatar = this.get("avatar").toString().removeQuotes(),
            coverImage = this.get("coverImage").toString().removeQuotes() ?: "",
            username = this.get("username").toString().removeQuotes() ?: "",
            notPublic = this.get("notPublic").asBoolean,
            postCount = this.get("postCount").asInt,
            following = this.get("following").asInt,
            followers = this.get("followers").asInt,
            userId = userData.get("id").toString().removeQuotes() ?: "",
            email = userData.get("email").toString().removeQuotes() ?: "",
            firstName = userData.get("firstName").toString().removeQuotes() ?: "",
            surname =  userData.get("surname").toString().removeQuotes() ?: "",
            middleName = userData.get("middleName").toString().removeQuotes() ?: "",
            profileImage = userData.get("profileImage").toString().removeQuotes() ?: "",
            dateOfBirth =  userData.get("dateOfBirth").toString().removeQuotes() ?: "",
            gender = userData.get("gender").toString().removeQuotes() ?: "",
            district = userData.get("district").toString().removeQuotes() ?: "",
            address = userData.get("address").toString().removeQuotes() ?: "",
            phoneNumber = userData.get("phoneNumber").toString().removeQuotes() ?: "",
            authId = userData.get("userId").toString().removeQuotes() ?: "",
            city = userData.get("city").toString().removeQuotes() ?: "",
            corporate = userData.get("corporate").asBoolean,
    )
}

fun JsonObject.getTempWallet():TempWallet{
        return TempWallet(
                id = this.get("id").asInt,
                accountNo = this.get("accountNo").toString().removeQuotes(),
                accountName = this.get("accountName").toString().removeQuotes(),
                balance = this.get("balance").asDouble,
                accountType = this.get("accountType").toString().removeQuotes(),
                default = this.get("default").asBoolean
        )
}

fun JsonObject.getLogInInfo(): LogInInfo{
        return LogInInfo(
                id = this.get("id").toString().removeQuotes(),
                ip = this.get("ip").toString().removeQuotes(),
                device = this.get("device").toString().removeQuotes(),
                city = this.get("city").toString().removeQuotes(),
                province = this.get("province").toString().removeQuotes(),
                country = this.get("country").toString().removeQuotes(),
                logInDate = this.get("loginDate").toString().removeQuotes()
        )
}

fun JsonObject.getCards(): UserBankCard {
        return UserBankCard(
                reference = this.get("reference").toString().removeQuotes(),
                authCode = this.get("auth").toString().removeQuotes(),
                accountName = this.get("accountName").toString().removeQuotes(),
                expiryMonth = this.get("expiryMonth").toString().removeQuotes(),
                walletFunded = this.get("walletFunded").asBoolean,
                walletAccountNumber = this.get("walletAccountNumber").toString().removeQuotes(),
                expiryYear = this.get("expiryYear").toString().removeQuotes(),
                type = this.get("type").toString().removeQuotes(),
                last4digit = this.get("last4Digits").toString().removeQuotes(),
                cardNumber = this.get("cardNumber").toString().removeQuotes()
        )
}

fun JsonObject.getAllCardsAsync(): UserBankCardDetails {
        return UserBankCardDetails(
                accountName = this.get("accountName")?.toString().removeQuotes(),
                authCode = this.get("authCode")?.toString().removeQuotes(),
                cardNumber = this.get("cardNumber")?.toString().removeQuotes(),
                email = this.get("expiryYear").toString().removeQuotes(),
                expiryMonth = this.get("expiryMonth").toString().removeQuotes(),
                expiryYear = this.get("expiryYear").toString().removeQuotes(),
                last4digit = this.get("last4digit")?.toString().removeQuotes(),
                reference = this.get("reference")?.toString().removeQuotes(),
                type = this.get("type").toString().removeQuotes(),
                userId = this.get("userId").toString().removeQuotes(),
                walletAccountNumber = this.get("walletAccountNumber").toString().removeQuotes(),
                walletFunded = this.get("walletFunded")?.asBoolean ?: false,



                )
}