package com.wayapaychat.core.models

import com.google.gson.annotations.SerializedName

data class UserInterestResponse(
    @SerializedName("data")
    var `data`: List<String>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("timestamp")
    var timestamp: Long?
)

data class InterestSuggestionResponse(
    @SerializedName("data")
    var `data`: List<InterestSuggestionResponseData>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("timestamp")
    var timestamp: Long?
)

data class InterestSuggestionResponseData(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)

data class FollowInterestRequest(
    @SerializedName("data")
    var `data`: List<String>?
)

data class UnfollowIntrestRequest(
    @SerializedName("id")
    var id: String?
)
