package com.wayapaychat.repository.impl.auth

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.auth.LoginUser
import com.wayapaychat.domain.repository.auth.AuthRepository
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import com.wayapaychat.repository.mappers.auth.toLoginUser
import com.wayapaychat.repository.remote.auth.IAuthRemote
import com.wayapaychat.repository.utils.constants.TYPE_CORPORATE
import com.wayapaychat.repository.utils.constants.TYPE_PERSONAL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

//class RiderRepositoryImpl @Inject constructor(
class IAuthRepositoryImpl(
    private val authRemote: IAuthRemote,
    private val iAuthenticationLocal: IAuthenticationLocal,
    private val iWayaPreferenceRepository: IWayaPreferenceRepository
) : AuthRepository {

    override suspend fun login(request: HashMap<String, Any>): Flow<BaseDomainModel<LoginUser>> {
        return authRemote.loginUser2(request).map {
            if (it.successful) {
                val userType = if (it.data!!.corporate) TYPE_CORPORATE else TYPE_PERSONAL
                iAuthenticationLocal.saveUserDetails(it.data)
                iWayaPreferenceRepository.saveLoggedInUserType(userType)
                iWayaPreferenceRepository.saveToken(userType, it.data.token)
            }

            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data?.toLoginUser()
            )
        }
    }

    override suspend fun registerUser(request: HashMap<String, Any>): Flow<BaseDomainModel<String>> =
        authRemote.registerUser(request).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }

    override suspend fun resetPassword(
        userId: String,
        newPassword: String,
        oldPassword: String
    ): Flow<BaseDomainModel<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun register(rideId: String): Flow<BaseDomainModel<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun createPin(request: HashMap<String, Any>): Flow<BaseDomainModel<Boolean>> =
        authRemote.createUserPin(request).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }

    override suspend fun verifyPhoneOtp(request: HashMap<String, Any>): Flow<BaseDomainModel<Boolean>> =
        authRemote.verifyPhoneOtp(request).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }

    override suspend fun verifyUserPin(
        userId: String,
        userPin: String
    ): Flow<BaseDomainModel<Boolean>> =
        authRemote.verifyUserPin(userId, userPin).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }

    override suspend fun resendOtpToPhone(
        phoneNumber: String,
        emailAddress: String
    ): Flow<BaseDomainModel<Boolean>> =
        authRemote.resendOtpToPhone(phoneNumber, emailAddress).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }

    override suspend fun changePassword(request: HashMap<String, Any>): Flow<BaseDomainModel<String>> =
        authRemote.changePassword(request).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }

    override suspend fun saveOnBoardingStatus(hasBeenOnBoarded: Boolean): Flow<String> =
        iAuthenticationLocal.saveOnBoardingStatus(hasBeenOnBoarded)

    override suspend fun saveUserLandingPage(landingConstant: Int): Flow<Boolean> =
        flow {
            iWayaPreferenceRepository.setUserLandingSelection(landingConstant)
            emit(true)
        }

    override suspend fun getUserLandingPage(): Flow<Int> = flow {
        emit(iWayaPreferenceRepository.getUserLandingSelection())
    }

    override suspend fun getLoggedInUserData(): LoginUser =
        iAuthenticationLocal.getSavedUserData().toLoginUser()

    override suspend fun forgotPassword(userEmail: String): Flow<BaseDomainModel<String>> =
        authRemote.forgotPassword(userEmail).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }

    override fun getOnBoardingStatus(): Flow<Boolean> =
        iAuthenticationLocal.hasUserBeenFullyOnBoarded()

    override suspend fun verifyLoggedInUserPin(userPin: String): BaseDomainModel<Boolean> {
        val response =
            authRemote.verifyUserPin(
                userPin
            )

        return BaseDomainModel(
            successful = response.successful,
            message = response.message,
            data = response.data
        )
    }

    override suspend fun saveUserTypeAndToken(
        userType: Int,
        userToken: String
    ): BaseDomainModel<Boolean> {
        iWayaPreferenceRepository.saveLoggedInUserType(userType)
        iWayaPreferenceRepository.saveToken(userType, userToken)

        return BaseDomainModel(
            true,
            null,
            "Saved successfully"
        )
    }
}
