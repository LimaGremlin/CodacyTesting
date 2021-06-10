package com.wayapaychat.repository.models.profile

data class UserProfileEntity(
    var email: String,
    var phoneNumber: String,
    var firstName: String,
    var lastName: String = "",
    var address: String = "",
    var dateOfBirth: String = "",
    var district: String = "",
    var gender: String = "",
    var middleName: String = "",
    var surname: String = "",
    var profileImage: String = "",
    var otherDetails: OtherDetailsEntity
)
