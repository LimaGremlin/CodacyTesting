package com.wayapaychat.repository.remote.auth

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.auth.LoginDataEntity
import com.wayapaychat.repository.models.auth.UserEntity
import kotlinx.coroutines.flow.Flow

interface IAuthRemote {
    suspend fun cancelRide(rideId: String): Flow<BaseRepositoryModel<String>>

    suspend fun registerUser(request: HashMap<String, Any>): Flow<BaseRepositoryModel<String>>

    suspend fun changePassword(request: HashMap<String, Any>): Flow<BaseRepositoryModel<String>>

    suspend fun verifyPhoneOtp(request: HashMap<String, Any>): Flow<BaseRepositoryModel<Boolean>>

    suspend fun loginUser(request: HashMap<String, Any>): Flow<BaseRepositoryModel<UserEntity>>

    suspend fun createUserPin(request: HashMap<String, Any>): Flow<BaseRepositoryModel<Boolean>>

    suspend fun resendOtpToPhone(
        phoneNumber: String,
        email: String
    ): Flow<BaseRepositoryModel<Boolean>>

    suspend fun verifyUserPin(userId: String, userPin: String): Flow<BaseRepositoryModel<Boolean>>

    suspend fun verifyUserPin(userPin: String): BaseRepositoryModel<Boolean>

    suspend fun forgotPassword(userEmail: String): Flow<BaseRepositoryModel<String>>

    suspend fun loginUser2(request: HashMap<String, Any>): Flow<BaseRepositoryModel<LoginDataEntity>>
}
