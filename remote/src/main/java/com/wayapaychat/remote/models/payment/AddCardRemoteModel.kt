package com.wayapaychat.remote.models.payment

import com.google.gson.annotations.SerializedName

data class AddCardRemoteModel(

	@field:SerializedName("timeStamp")
	val timeStamp: Long? = null,

	@field:SerializedName("data")
	val addCardDataModel: AddCardDataModel? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class AddCardDataModel(

	@field:SerializedName("reference")
	val reference: String? = null,

	@field:SerializedName("display_text")
	val displayText: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
