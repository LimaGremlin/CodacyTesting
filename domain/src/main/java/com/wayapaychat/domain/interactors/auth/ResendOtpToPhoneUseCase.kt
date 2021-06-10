package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResendOtpToPhoneUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<ResendOtpToPhoneUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val phoneNumber: String,
        val email: String,
    ) {
        companion object {
            @JvmStatic
            fun make(
                phoneNumber: String,
                email: String,
            ): Parameter =
                Parameter(
                    phoneNumber,
                    email,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<Boolean>> =
        param?.let {
            IAuthRepository.resendOtpToPhone(it.phoneNumber, it.email)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
