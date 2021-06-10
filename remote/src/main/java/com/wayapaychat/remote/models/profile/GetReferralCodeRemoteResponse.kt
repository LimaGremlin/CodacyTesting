package com.wayapaychat.remote.models.profile

import com.google.gson.annotations.SerializedName

data class GetReferralCodeRemoteResponse(

	@field:SerializedName("data")
	val referralCodeDataRemoteModel: ReferralCodeDataRemoteModel? = null,

	@field:SerializedName("httpStatus")
	val httpStatus: Any? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ReferralCodeDataRemoteModel(
	@field:SerializedName("referralCode")
	val referralCode: String? = null
)
