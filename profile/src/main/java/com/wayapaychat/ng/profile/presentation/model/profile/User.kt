package com.wayapaychat.ng.profile.presentation.model.profile

data class User(
    var email: String,
    var id: Int,
    var phoneNumber: String,
    var firstName: String,
    var lastName: String,
    var address: String,
    var dateOfBirth: String,
    var district: String,
    var gender: String,
    var middleName: String,
    var surname: String,
    var profileImage: String,
    val otherDetails: OtherDetails?,
    var corporate: Boolean
)
