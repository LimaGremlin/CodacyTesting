package com.wayapaychat.remote.impl.auth

import com.wayapaychat.remote.mappers.auth.toUserLoginEntity
import com.wayapaychat.remote.models.auth.LoginnDataRemoteModel
import com.wayapaychat.remote.services.auth.AuthService
import com.wayapaychat.remote.utils.WayaAppExceptionHandler.getErrorFromThrowable
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.auth.LoginDataEntity
import com.wayapaychat.repository.models.auth.UserEntity
import com.wayapaychat.repository.remote.auth.IAuthRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class AuthRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit,
    @Named(value = WayaAppRemoteConstants.PASSWORD_RETROFIT) passwordRetrofit: Retrofit,
) : IAuthRemote {

    private val authService = retrofit.create(AuthService::class.java)
    private val passwordService = passwordRetrofit.create(AuthService::class.java)

    override suspend fun cancelRide(rideId: String): Flow<BaseRepositoryModel<String>> {
        return flow {
//            authService.cancelRide(rideId).map {
//                BaseRepositoryModel(
//                    successful = it.success,
//                    message = it.message,
//                    data = it.data?.response
//                )
//            }
        }
    }

    override suspend fun registerUser(request: HashMap<String, Any>): Flow<BaseRepositoryModel<String>> {
        return flow {
            try {
                val response = authService.registerPersonalUser(request)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.message,
                        data = response.data?.data!!
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = "An Error Occurred"
                    )
                )
            }
        }
    }

    override suspend fun changePassword(request: HashMap<String, Any>): Flow<BaseRepositoryModel<String>> {
        return flow {
            try {
                val response = passwordService.changePassword(request)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.message,
                        data = response.data?.message
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = "An Error Occurred"
                    )
                )
            }
        }
    }

    override suspend fun verifyPhoneOtp(request: HashMap<String, Any>): Flow<BaseRepositoryModel<Boolean>> {
        return flow {
            try {
                val response = authService.verifyPhoneOtp(request)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.data?.message,
                        data = response.data?.status
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = false
                    )
                )
            }
        }
    }

    override suspend fun loginUser(request: HashMap<String, Any>): Flow<BaseRepositoryModel<UserEntity>> {
        TODO("Something else")
//        flow {
//            try {
//                val response = authService.login(request)
//                emit(
//                    BaseRepositoryModel(
//                        successful = response.success,
//                        message = response.message,
//                        data = response.data?.loginnDataRemoteModel?.toUserLoginEntity()
//                    )
//                )
//            } catch (throwable: Throwable) {
//                emit(
//                    BaseRepositoryModel(
//                        successful = false,
//                        message = getErrorFromThrowable(throwable),
//                        data = LoginnDataRemoteModel().toUserLoginEntity()
//                    )
//                )
//            }
//        }
    }

    override suspend fun createUserPin(request: HashMap<String, Any>): Flow<BaseRepositoryModel<Boolean>> =
        flow {
            try {
                val response = authService.createPin(request)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.message,
                        data = response.data?.status
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = false
                    )
                )
            }
        }

    override suspend fun resendOtpToPhone(
        phoneNumber: String,
        email: String
    ): Flow<BaseRepositoryModel<Boolean>> =
        flow {
            try {
                val response = authService.resendOtpToPhone(phoneNumber, email)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.message,
                        data = response.data?.status
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = false
                    )
                )
            }
        }

    override suspend fun verifyUserPin(
        userId: String,
        userPin: String
    ): Flow<BaseRepositoryModel<Boolean>> =
        flow {
            try {
                val response = authService.verifyPin(userPin)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.message,
                        data = response.data?.status
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = false
                    )
                )
            }
        }

    override suspend fun verifyUserPin(
        userPin: String
    ): BaseRepositoryModel<Boolean> {
        try {
            val response = authService.verifyPin(userPin)
            return BaseRepositoryModel(
                successful = response.success,
                message = response.message,
                data = response.data?.status
            )
        } catch (throwable: Throwable) {
            return BaseRepositoryModel(
                successful = false,
                message = getErrorFromThrowable(throwable),
                data = false
            )
        }
    }

    override suspend fun forgotPassword(userEmail: String): Flow<BaseRepositoryModel<String>> =
        flow {
            try {
                val response = passwordService.forgotPassword(userEmail)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.message,
                        data = response.data?.message
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = "Empty message"
                    )
                )
            }
        }

    override suspend fun loginUser2(request: HashMap<String, Any>): Flow<BaseRepositoryModel<LoginDataEntity>> =
        flow {
            try {
                val response = authService.login(request)
                emit(
                    BaseRepositoryModel(
                        successful = response.success,
                        message = response.message,
                        data = response.data?.loginnDataRemoteModel?.toUserLoginEntity()
                    )
                )
            } catch (throwable: Throwable) {
                emit(
                    BaseRepositoryModel(
                        successful = false,
                        message = getErrorFromThrowable(throwable),
                        data = LoginnDataRemoteModel().toUserLoginEntity()
                    )
                )
            }
        }
}
