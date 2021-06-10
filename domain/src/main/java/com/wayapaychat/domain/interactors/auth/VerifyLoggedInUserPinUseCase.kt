package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyLoggedInUserPinUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParamsNoFlow<VerifyLoggedInUserPinUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val userPin: String,
    ) {
        companion object {
            @JvmStatic
            fun make(
                userPin: String,
            ): Parameter =
                Parameter(
                    userPin,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Boolean> =
        param?.let {
            IAuthRepository.verifyLoggedInUserPin(it.userPin)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
