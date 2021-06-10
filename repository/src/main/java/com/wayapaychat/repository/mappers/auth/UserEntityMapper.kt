package com.wayapaychat.repository.mappers.auth

import com.wayapaychat.domain.models.auth.LoginUser
import com.wayapaychat.domain.models.profile.OtherDetailsDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.repository.models.auth.LoginDataEntity
import com.wayapaychat.repository.models.auth.UserEntity
import com.wayapaychat.repository.models.profile.OtherDetailsEntity

fun LoginDataEntity.toLoginUser(): LoginUser = LoginUser(
    email = user.email,
    phoneNumber = user.phoneNumber,
    pinCreated,
    user.id,
    user.firstName,
    user.lastName
)

fun UserEntity.toDomainModel(corporate: Boolean): UserDomainModel =
    UserDomainModel(
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
        otherDetails?.toDomainModel(),
        corporate
    )

fun OtherDetailsEntity.toDomainModel(): OtherDetailsDomainModel =
    OtherDetailsDomainModel(
        businessType, organisationName, organisationType
    )
