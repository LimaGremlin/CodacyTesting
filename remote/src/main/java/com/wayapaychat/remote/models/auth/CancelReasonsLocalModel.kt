package com.wayapaychat.remote.models.auth

import com.google.gson.annotations.SerializedName
import com.wayapaychat.remote.models.ReasonsLocalModel

data class CancelReasonsLocalModel(
	@field:SerializedName("data")
	val data: List<ReasonsLocalModel>? = null,
	@field:SerializedName("data")
	val loginId: String? = null,
)
