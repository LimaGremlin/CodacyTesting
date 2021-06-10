package com.wayapaychat.remote.models.auth

import com.google.gson.annotations.SerializedName

data class LoginRemoteModel(
    @SerializedName("pinCreated")
    var pinCreated: Boolean? = null,

    @SerializedName("roles")
    var roles: List<String>? = null,

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("user")
    var userRemoteModel: UserRemoteModel? = null
)

data class UserRemoteModel(
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("phoneNumber")
    var phoneNumber: Long? = null
)
