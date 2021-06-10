package com.wayapaychat.local.mappers

import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.repository.models.auth.UserEntity
import com.wayapaychat.repository.models.auth.LoginDataEntity
import com.wayapaychat.repository.models.profile.OtherDetailsEntity

fun UserDataLocalModel.toUserEntity(): LoginDataEntity {
    val loginUserEntity = UserEntity(
        email,
        id,
        phoneNumber,
        firstName,
        lastName,
        address,
        dateOfBirth,
        district,
        gender,
        middleName,
        surname,
        profileImage,
        if (corporate)
            OtherDetailsEntity(
                businessType ?: "",
                organisationName ?: "",
                organisationType ?: ""
            )
        else
            null
    )

    return LoginDataEntity(
        false,
        roles,
        "Fake-Token",
        corporate,
        loginUserEntity
    )
}

fun LoginDataEntity.toUserDataLocalModel(): UserDataLocalModel = UserDataLocalModel(
    id = user.id,
    phoneNumber = user.phoneNumber,
    email = user.email,
    roles = roles,
    corporate = corporate,
    firstName = user.firstName,
    lastName = user.lastName,
    address = user.address,
    dateOfBirth = user.dateOfBirth,
    district = user.district,
    gender = user.gender,
    middleName = user.middleName,
    surname = user.surname,
    profileImage = user.profileImage,
    businessType = user.otherDetails?.businessType,
    organisationName = user.otherDetails?.organisationName,
    organisationType = user.otherDetails?.organisationType
)
