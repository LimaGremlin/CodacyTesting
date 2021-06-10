package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<ChangePasswordUseCase.Parameter, BaseDomainModel<String>>() {

    data class Parameter(
        val email: String,
        val newPassword: String,
        val otp: String,
    ) {
        companion object {
            @JvmStatic
            fun make(
                email: String,
                newPassword: String,
                otp: String
            ): Parameter =
                Parameter(
                    email,
                    newPassword,
                    otp
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<String>> =
        param?.let {
            IAuthRepository.changePassword(
                hashMapOf(
                    "email" to it.email,
                    "newPassword" to it.newPassword,
                    "otp" to it.otp
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
