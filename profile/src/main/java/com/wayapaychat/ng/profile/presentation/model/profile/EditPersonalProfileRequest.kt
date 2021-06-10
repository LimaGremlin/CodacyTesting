package com.wayapaychat.ng.profile.presentation.model.profile

data class EditPersonalProfileRequest(
    var address: String,
    var dateOfBirth: String,
    var district: String,
    var email: String,
    var firstName: String,
    var gender: String,
    var middleName: String,
    var phoneNumber: String,
    var surname: String
)