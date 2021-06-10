package com.wayapaychat.remote.models.profile

import com.google.gson.annotations.SerializedName

data class UserProfileRemoteModel(

	@field:SerializedName("data")
	val userProfileDataModel: UserProfileDataModel? = null,

	@field:SerializedName("httpStatus")
	val httpStatus: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class UserProfileDataModel(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("otherDetails")
	val userProfileOtherDetailsRemoteModel: UserProfileOtherDetailsRemoteModel? = null,

	@field:SerializedName("dateOfBirth")
	val dateOfBirth: String? = null,

	@field:SerializedName("profileImage")
	val profileImage: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("corporate")
	val corporate: Boolean? = null,

	@field:SerializedName("surname")
	val surname: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("middleName")
	val middleName: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class UserProfileOtherDetailsRemoteModel(

	@field:SerializedName("organisationName")
	val organisationName: String? = null,

	@field:SerializedName("businessType")
	val businessType: String? = null,

	@field:SerializedName("organisationType")
	val organisationType: String? = null
)
