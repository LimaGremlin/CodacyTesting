package com.wayapaychat.repository.models.auth

data class PinVerificationDataEntity(
	val firstName: String,
	val phoneNumber: String,
	val role: Any,
	val dateCreated: String,
	val corporate: Boolean,
	val surname: String,
	val pinCreated: Boolean,
	val id: Int,
	val referenceCode: String,
	val email: String
)
