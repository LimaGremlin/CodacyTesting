package com.wayapaychat.remote.models.auth

import com.google.gson.annotations.SerializedName

data class PinVerificationRemoteModel(

	@field:SerializedName("timeStamp")
	val timeStamp: String? = null,

	@field:SerializedName("data")
	val pinVerificationDataRemoteModel: PinVerificationDataRemoteModel? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class PinVerificationDataRemoteModel(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("role")
	val role: Any? = null,

	@field:SerializedName("dateCreated")
	val dateCreated: String? = null,

	@field:SerializedName("corporate")
	val corporate: Boolean? = null,

	@field:SerializedName("surname")
	val surname: String? = null,

	@field:SerializedName("pinCreated")
	val pinCreated: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("referenceCode")
	val referenceCode: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
