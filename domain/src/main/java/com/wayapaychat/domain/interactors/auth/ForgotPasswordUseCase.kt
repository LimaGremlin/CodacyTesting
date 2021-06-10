package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<ForgotPasswordUseCase.Parameter, BaseDomainModel<String>>() {

    data class Parameter(
        val email: String,
    ) {
        companion object {
            @JvmStatic
            fun make(
                email: String,
            ): Parameter =
                Parameter(
                    email,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<String>> =
        param?.let {
            IAuthRepository.forgotPassword(it.email)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
