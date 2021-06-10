package com.wayapaychat.remote.mappers.auth

import com.wayapaychat.remote.models.auth.LoginnDataRemoteModel
import com.wayapaychat.remote.models.auth.UserRemoteeModel
import com.wayapaychat.remote.models.profile.OtherDetailsRemoteModel
import com.wayapaychat.remote.utils.safeBoolean
import com.wayapaychat.remote.utils.safeInt
import com.wayapaychat.remote.utils.safeString
import com.wayapaychat.repository.models.auth.UserEntity
import com.wayapaychat.repository.models.auth.LoginDataEntity
import com.wayapaychat.repository.models.profile.OtherDetailsEntity

fun LoginnDataRemoteModel.toUserLoginEntity(): LoginDataEntity =
    LoginDataEntity(
        pinCreated = pinCreated.safeBoolean(),
        token = token.safeString(),
        roles = roles?.map {
            it.safeString()
        } ?: emptyList(),
        user = userRemoteeModel?.toUserEntity()!!,
        corporate = corporate.safeBoolean()
    )

fun UserRemoteeModel.toUserEntity(): UserEntity =
    UserEntity(
        email.safeString(),
        id.safeInt(),
        phoneNumber.safeString(),
        firstName,
        lastName,
        address,
        dateOfBirth,
        district,
        gender,
        middleName,
        surname,
        profileImage,
        otherDetails?.toEntity()
    )

fun OtherDetailsRemoteModel.toEntity(): OtherDetailsEntity =
    OtherDetailsEntity(
        businessType, organisationName, organisationType
    )
