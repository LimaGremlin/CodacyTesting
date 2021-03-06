package com.wayapaychat.remote.base

import com.google.gson.annotations.SerializedName

/**
 * Created by ayokunlepaul on 2019-07-17
 */
data class BaseNetworkModel<T> (
    @SerializedName("data", alternate = ["response"]) val data: T? = null,
    @SerializedName("success", alternate = ["status"]) val success: Boolean,
    val error: Boolean? = false,
    val message: String? = null,
    val code: Int? = 0
)
