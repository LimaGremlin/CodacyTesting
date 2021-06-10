package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SaveUserLandingUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<SaveUserLandingUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val userSelection: Int
    ) {
        companion object {
            @JvmStatic
            fun make(
                userLandingSelection: Int
            ): Parameter =
                Parameter(
                    userLandingSelection
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<Boolean>> =
        param?.let {
            IAuthRepository.saveUserLandingPage(it.userSelection).map {
                BaseDomainModel(true, it, "Data Retrieved")
            }
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
