package com.wayapaychat.remote.models.payment

import com.google.gson.annotations.SerializedName

data class UserBankCardsRemoteModel(

	@field:SerializedName("timeStamp")
	val timeStamp: Long? = null,

	@field:SerializedName("data")
	val data: List<UserBankCardDataRemoteModel>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class UserBankCardDataRemoteModel(

	@field:SerializedName("reference")
	val reference: String? = null,

	@field:SerializedName("authCode")
	val authCode: Any? = null,

	@field:SerializedName("accountName")
	val accountName: String? = null,

	@field:SerializedName("expiryMonth")
	val expiryMonth: String? = null,

	@field:SerializedName("walletFunded")
	val walletFunded: Boolean? = null,

	@field:SerializedName("walletAccountNumber")
	val walletAccountNumber: String? = null,

	@field:SerializedName("expiryYear")
	val expiryYear: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("last4digit")
	val last4digit: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("cardNumber")
	val cardNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
