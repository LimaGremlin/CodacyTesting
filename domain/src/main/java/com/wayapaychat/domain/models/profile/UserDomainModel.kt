package com.wayapaychat.domain.models.profile

data class UserDomainModel(
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
    var otherDetails: OtherDetailsDomainModel?,
    var corporate: Boolean
)
