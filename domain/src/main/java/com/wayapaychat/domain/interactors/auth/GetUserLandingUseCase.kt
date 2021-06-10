package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithNoParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserLandingUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithNoParams<BaseDomainModel<Int>>() {

    override suspend fun buildUseCase(): Flow<BaseDomainModel<Int>> =
        IAuthRepository.getUserLandingPage().map {
            BaseDomainModel(true, it, "Data Retrieved")
        }
}
