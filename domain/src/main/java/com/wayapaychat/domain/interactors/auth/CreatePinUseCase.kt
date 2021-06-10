package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatePinUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<CreatePinUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val userId: Int,
        val email: String,
        val pin: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                userId: Int,
                email: String,
                pin: String
            ): Parameter =
                Parameter(
                    userId,
                    email,
                    pin,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<Boolean>> =
        param?.let {
            IAuthRepository.createPin(
                hashMapOf(
                    "email" to it.email,
                    "pin" to it.pin,
                    "userId" to it.userId
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
