package com.wayapaychat.repository.mappers.profile

import com.wayapaychat.domain.models.profile.UserProfileModel
import com.wayapaychat.domain.models.profile.UserProfileOtherDetailsModel
import com.wayapaychat.repository.models.profile.UserProfileEntityModel
import com.wayapaychat.repository.models.profile.UserProfileOtherDetailsEntityModel

fun UserProfileEntityModel.toUserProfileModel(): UserProfileModel =
    UserProfileModel(
        address,
        gender,
        city,
        userProfileOtherDetailsRemoteModel?.toUserProfileOtherDetailsModel(),
        dateOfBirth,
        profileImage,
        userId,
        firstName,
        phoneNumber,
        corporate,
        surname,
        district,
        middleName,
        id,
        email
    )

fun UserProfileOtherDetailsEntityModel.toUserProfileOtherDetailsModel(): UserProfileOtherDetailsModel =
    UserProfileOtherDetailsModel(
        organisationName,
        businessType,
        organisationType
    )
