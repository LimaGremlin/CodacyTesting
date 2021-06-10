package com.wayapaychat.repository.local.auth

import com.wayapaychat.repository.models.auth.LoginDataEntity
import com.wayapaychat.repository.models.auth.UserEntity
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface IAuthenticationLocal {
    fun isUserFullyRegistered(): Observable<Boolean>

    suspend fun saveOnBoardingStatus(status: Boolean): Flow<String>

    fun hasUserBeenFullyOnBoarded(): Flow<Boolean>

    suspend fun saveUserDetails(userData: LoginDataEntity): Boolean

    suspend fun getSavedUserData(): LoginDataEntity
}
