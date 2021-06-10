package com.wayapaychat.repository.models.auth

import com.wayapaychat.repository.models.profile.OtherDetailsEntity

data class UserEntity(
    var email: String,
    var id: Int,
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
    var otherDetails: OtherDetailsEntity?
)
