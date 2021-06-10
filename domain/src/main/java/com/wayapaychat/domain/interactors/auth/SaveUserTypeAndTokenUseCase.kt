package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import com.wayapaychat.domain.utils.Constants.TYPE_CORPORATE
import com.wayapaychat.domain.utils.Constants.TYPE_PERSONAL
import javax.inject.Inject

class SaveUserTypeAndTokenUseCase @Inject constructor(
    private val iAuthRepository: AuthRepository
) : UseCaseWithParamsNoFlow<SaveUserTypeAndTokenUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val isCooperate: Boolean,
        val userToken: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                isCorporate: Boolean,
                userToken: String
            ): Parameter =
                Parameter(
                    isCorporate,
                    userToken
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Boolean> =
        param?.let {
            iAuthRepository.saveUserTypeAndToken(
                if (it.isCooperate) TYPE_CORPORATE else TYPE_PERSONAL,
                it.userToken
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}

