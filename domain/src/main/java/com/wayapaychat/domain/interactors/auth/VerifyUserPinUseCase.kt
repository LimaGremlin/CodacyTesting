package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyUserPinUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<VerifyUserPinUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val userId: String,
        val userPin: String,
    ) {
        companion object {
            @JvmStatic
            fun make(
                userId: String,
                userPin: String,
            ): Parameter =
                Parameter(
                    userId,
                    userPin,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<Boolean>> =
        param?.let {
            IAuthRepository.verifyUserPin(it.userId, it.userPin)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
