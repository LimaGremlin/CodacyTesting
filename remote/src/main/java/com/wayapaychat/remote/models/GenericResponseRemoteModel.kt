package com.wayapaychat.remote.models

import com.google.gson.annotations.SerializedName

data class GenericResponseRemoteModel<Data>(

	@field:SerializedName("timeStamp")
	val timeStamp: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
