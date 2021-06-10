package com.wayapaychat.core.newtwork

import java.io.Serializable

class PersonalUser(
    val corporate: Boolean = false,
    val email: String = "",
    val firstName: String = "",
    var password: String = "",
    val phoneNumber: String = "",
    val referenceCode: String = "",
    val surname: String = ""
): Serializable

class CreatePin(
    val email: String = "",
    val pin: Int = -1,
    val userId: Int = -1
)

class LogInDetails(
    val admin:Boolean = true,
    val email:String = "",
    val password: String = "",
): Serializable

data class LogInResponse(
    val code: Int = 0,
    val message: String = "",
    val status: Boolean = false
):Serializable