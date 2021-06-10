package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyPhoneOtpUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<VerifyPhoneOtpUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val phoneNumber: String,
        val otp: Int
    ) {
        companion object {
            @JvmStatic
            fun make(
                phoneNumber: String,
                otp: Int
            ): Parameter =
                Parameter(
                    phoneNumber,
                    otp
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<Boolean>> =
        param?.let {
            IAuthRepository.verifyPhoneOtp(
                hashMapOf(
                    "otp" to it.otp,
                    "phone" to it.phoneNumber
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
