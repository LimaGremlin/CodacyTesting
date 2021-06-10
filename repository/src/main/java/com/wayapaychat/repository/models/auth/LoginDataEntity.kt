package com.wayapaychat.repository.models.auth

data class LoginDataEntity(
    var pinCreated: Boolean,
    var roles: List<String>,
    var token: String,
    var corporate: Boolean,
    var user: UserEntity
)
