package com.wayapaychat.repository.models.payment

data class UserBankCardDataEntity(

	val reference: String,
	val authCode: Any?,
	val accountName: String,
	val expiryMonth: String,
	val walletFunded: Boolean,
	val walletAccountNumber: String,
	val expiryYear: String,
	val type: String,
	val last4digit: String,
	val cardNumber: String,
)
