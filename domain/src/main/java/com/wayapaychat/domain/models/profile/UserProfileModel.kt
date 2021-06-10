package com.wayapaychat.domain.models.profile

data class UserProfileModel(
    val address: String,
    val gender: String,
    val city: String,
    val userProfileOtherDetailsRemoteModel: UserProfileOtherDetailsModel?,
    val dateOfBirth: String,
    val profileImage: String,
    val userId: String,
    val firstName: String,
    val phoneNumber: String,
    val corporate: Boolean,
    val surname: String,
    val district: String,
    val middleName: String,
    val id: String,
    val email: String
)

data class UserProfileOtherDetailsModel(
    val organisationName: String,
    val businessType: String,
    val organisationType: String
)
