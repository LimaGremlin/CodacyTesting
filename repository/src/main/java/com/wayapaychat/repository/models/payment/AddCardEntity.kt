package com.wayapaychat.repository.models.payment

data class AddCardEntity(
	val reference: String,
	val displayText: String,
	val status: String
)
