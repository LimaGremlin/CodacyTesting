package com.wayapaychat.domain.interactors.onboarding

import com.wayapaychat.domain.interactors.UseCaseWithNoParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnBoardingStatusUseCase @Inject constructor(
    private val iAuthRepository: AuthRepository
) : UseCaseWithNoParams<BaseDomainModel<Boolean>>() {

    override suspend fun buildUseCase(): Flow<BaseDomainModel<Boolean>> {
        return iAuthRepository.getOnBoardingStatus().map {
            BaseDomainModel(
                successful = true,
                data = it,
                message = ""
            )
        }
    }
}
