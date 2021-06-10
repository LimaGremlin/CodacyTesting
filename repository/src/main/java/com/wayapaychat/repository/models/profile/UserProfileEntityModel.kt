package com.wayapaychat.repository.models.profile

data class UserProfileEntityModel(
    val address: String,
    val gender: String,
    val city: String,
    val userProfileOtherDetailsRemoteModel: UserProfileOtherDetailsEntityModel?,
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

data class UserProfileOtherDetailsEntityModel(
    val organisationName: String,
    val businessType: String,
    val organisationType: String
)
