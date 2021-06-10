package com.wayapaychat.repository.mappers.profile

import com.wayapaychat.repository.models.auth.UserEntity
import com.wayapaychat.repository.models.profile.UserProfileEntity

fun UserProfileEntity.toUserEntity(id: Int): UserEntity =
    UserEntity(
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
        otherDetails
    )