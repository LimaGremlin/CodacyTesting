package com.wayapaychat.core.models

import java.io.Serializable

data class SelectedPlace(
    var name:String = "",
    var address:String = "",
    var city:String = "",
    var state:String = "",
    var country:String = "",
    var countryCode:String = "",
    var postalCode:String = "",
    var knownName:String = "",
    var premises:String = "",
    var latitude:Double = 0.0,
    var longitude:Double = 0.0
): Serializable

data class LogInInfo(
    var id: String = "",
    var ip: String = "",
    var device: String = "",
    var city: String = "",
    var province: String = "",
    var country: String = "",
    var logInDate: String = "",
    var address: String = "${city}, ${province}, $country"
)