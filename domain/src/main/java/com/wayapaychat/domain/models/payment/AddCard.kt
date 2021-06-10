package com.wayapaychat.domain.models.payment

data class AddCard(
	val reference: String,
	val displayText: String,
	val status: String
)
