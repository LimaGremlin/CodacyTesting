package com.wayapaychat.domain.repository.auth

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.auth.LoginUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: HashMap<String, Any>): Flow<BaseDomainModel<LoginUser>>

    suspend fun registerUser(request: HashMap<String, Any>): Flow<BaseDomainModel<String>>

    suspend fun resetPassword(
        userId: String,
        newPassword: String,
        oldPassword: String
    ): Flow<BaseDomainModel<String>>

    suspend fun register(rideId: String): Flow<BaseDomainModel<String>>

    suspend fun createPin(request: HashMap<String, Any>): Flow<BaseDomainModel<Boolean>>

    suspend fun verifyPhoneOtp(request: HashMap<String, Any>): Flow<BaseDomainModel<Boolean>>

    suspend fun verifyUserPin(userId: String, userPin: String): Flow<BaseDomainModel<Boolean>>

    suspend fun resendOtpToPhone(
        phoneNumber: String,
        emailAddress: String
    ): Flow<BaseDomainModel<Boolean>>

    suspend fun changePassword(request: HashMap<String, Any>): Flow<BaseDomainModel<String>>

    suspend fun saveOnBoardingStatus(hasBeenOnBoarded: Boolean): Flow<String>

    suspend fun saveUserLandingPage(landingConstant: Int): Flow<Boolean>

    suspend fun getUserLandingPage(): Flow<Int>

    suspend fun getLoggedInUserData(): LoginUser

    suspend fun forgotPassword(userEmail: String): Flow<BaseDomainModel<String>>

    fun getOnBoardingStatus(): Flow<Boolean>

    suspend fun verifyLoggedInUserPin(userPin: String): BaseDomainModel<Boolean>

    suspend fun saveUserTypeAndToken(userType: Int, userToken: String): BaseDomainModel<Boolean>

}
