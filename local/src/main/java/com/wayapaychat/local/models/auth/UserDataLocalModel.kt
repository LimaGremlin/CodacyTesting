package com.wayapaychat.local.models.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "WAYA USER DATA"
)
class UserDataLocalModel(
    @PrimaryKey val id: Int = -1,
    val phoneNumber: String = "",
    val email: String = "",
    val roles: List<String> = listOf(),
    val corporate: Boolean = false,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var dateOfBirth: String = "",
    var district: String = "",
    var token: String = "",
    var gender: String = "",
    var middleName: String = "",
    var surname: String = "",
    var profileImage: String = "",
    var businessType: String? = "",
    var organisationName: String? = "",
    var organisationType: String? = ""
)
