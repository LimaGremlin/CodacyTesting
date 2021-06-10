package com.wayapaychat.remote.mappers.profile

import com.wayapaychat.remote.models.profile.OtherDetailsRemoteModel
import com.wayapaychat.remote.models.profile.UserProfileRemoteModel
import com.wayapaychat.remote.utils.safeString
import com.wayapaychat.repository.models.auth.UserEntity
import com.wayapaychat.repository.models.profile.OtherDetailsEntity

fun UserProfileRemoteModel.toUserEntity(id: Int): UserEntity =
    UserEntity(
        userProfileDataModel?.email.safeString(),
        id,
        userProfileDataModel?.phoneNumber.safeString(),
        userProfileDataModel?.firstName.safeString(),
        userProfileDataModel?.lastName.safeString(),
        userProfileDataModel?.address.safeString(),
        userProfileDataModel?.dateOfBirth.safeString(),
        userProfileDataModel?.district.safeString(),
        userProfileDataModel?.gender.safeString(),
        userProfileDataModel?.middleName.safeString(),
        userProfileDataModel?.surname.safeString(),
        userProfileDataModel?.profileImage.safeString(),
        otherDetails = userProfileDataModel?.userProfileOtherDetailsRemoteModel?.toOtherDetailsEntity()
    )

fun OtherDetailsRemoteModel.toEntity(): OtherDetailsEntity =
    OtherDetailsEntity(
        businessType, organisationName, organisationType
    )
