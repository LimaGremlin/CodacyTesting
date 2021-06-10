package com.wayapaychat.domain.interactors.onboarding

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveOnBoardingStatusUseCase @Inject constructor(
    private val iAuthRepository: AuthRepository
) : UseCaseWithParams<Boolean, BaseDomainModel<String>>() {

    override suspend fun buildUseCase(param: Boolean?): Flow<BaseDomainModel<String>> {
        return flow {
            param?.let {
                BaseDomainModel(
                    successful = false,
                    data = iAuthRepository.saveOnBoardingStatus(it),
                    message = "Save attempted"
                )
            }
        }
    }
}
