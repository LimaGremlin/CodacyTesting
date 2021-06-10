package com.wayapaychat.ng.profile.presentation.mappers

import com.wayapaychat.domain.models.profile.OtherDetailsDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.ng.profile.presentation.model.profile.OtherDetails
import com.wayapaychat.ng.profile.presentation.model.profile.User

fun UserDomainModel.toPresentation(): User =
    User(
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
        otherDetails?.toPresentation(),
        corporate
    )

fun OtherDetailsDomainModel.toPresentation(): OtherDetails =
    OtherDetails(
        businessType, organisationName, organisationType
    )