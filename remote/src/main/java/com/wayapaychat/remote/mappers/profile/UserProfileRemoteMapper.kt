package com.wayapaychat.remote.mappers.profile

import com.wayapaychat.remote.models.profile.UserProfileDataModel
import com.wayapaychat.remote.models.profile.UserProfileOtherDetailsRemoteModel
import com.wayapaychat.remote.utils.safeBoolean
import com.wayapaychat.remote.utils.safeString
import com.wayapaychat.repository.models.profile.OtherDetailsEntity
import com.wayapaychat.repository.models.profile.UserProfileEntityModel
import com.wayapaychat.repository.models.profile.UserProfileOtherDetailsEntityModel

fun UserProfileDataModel.toUserProfileEntityModel(): UserProfileEntityModel =
    UserProfileEntityModel(
        address.safeString(),
        gender.safeString(),
        city.safeString(),
        userProfileOtherDetailsRemoteModel?.toUserProfileOtherDetailsEntityModel(),
        dateOfBirth.safeString(),
        profileImage.safeString(),
        userId.safeString(),
        firstName.safeString(),
        phoneNumber.safeString(),
        corporate.safeBoolean(),
        surname.safeString(),
        district.safeString(),
        middleName.safeString(),
        id.safeString(),
        email.safeString()
    )

fun UserProfileOtherDetailsRemoteModel.toUserProfileOtherDetailsEntityModel(): UserProfileOtherDetailsEntityModel =
    UserProfileOtherDetailsEntityModel(
        organisationName.safeString(),
        businessType.safeString(),
        organisationType.safeString()
    )

fun UserProfileOtherDetailsRemoteModel.toOtherDetailsEntity(): OtherDetailsEntity =
    OtherDetailsEntity(
        organisationName.safeString(),
        businessType.safeString(),
        organisationType.safeString()
    )
