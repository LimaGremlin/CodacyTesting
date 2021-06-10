package com.wayapaychat.domain.models.payment

data class UserBankCard(
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
