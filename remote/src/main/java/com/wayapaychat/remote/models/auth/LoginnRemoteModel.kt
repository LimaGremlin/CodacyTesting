package com.wayapaychat.remote.models.auth

import com.google.gson.annotations.SerializedName
import com.wayapaychat.remote.models.profile.OtherDetailsRemoteModel

data class LoginnRemoteModel(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val loginnDataRemoteModel: LoginnDataRemoteModel? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class LoginnDataRemoteModel(

	@field:SerializedName("corporate")
	val corporate: Boolean? = null,

	@field:SerializedName("roles")
	val roles: List<String>? = null,

	@field:SerializedName("pinCreated")
	val pinCreated: Boolean? = null,

	@field:SerializedName("user")
	val userRemoteeModel: UserRemoteeModel? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class UserRemoteeModel(

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("firstName")
	var firstName: String = "",

	@field:SerializedName("lastName")
	var lastName: String = "",

	@field:SerializedName("address")
	var address: String = "",

	@field:SerializedName("dateOfBirth")
	var dateOfBirth: String = "",

	@field:SerializedName("district")
	var district: String = "",

	@field:SerializedName("gender")
	var gender: String = "",

	@field:SerializedName("middleName")
	var middleName: String = "",

	@field:SerializedName("surname")
	var surname: String = "",

	@field:SerializedName("profileImage")
	var profileImage: String = "",

	@field:SerializedName("otherDetails")
	var otherDetails: OtherDetailsRemoteModel? = null

)
