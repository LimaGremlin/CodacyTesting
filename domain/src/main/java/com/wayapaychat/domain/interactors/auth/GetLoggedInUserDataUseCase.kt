package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.auth.LoginUser
import com.wayapaychat.domain.repository.auth.AuthRepository
import javax.inject.Inject

class GetLoggedInUserDataUseCase @Inject constructor(
    private val iAuthRepository: AuthRepository,
) : UseCaseWithNoParamsNoFlow<BaseDomainModel<LoginUser>>() {
    override suspend fun buildUseCase(): BaseDomainModel<LoginUser> {
        return BaseDomainModel(
            successful = true,
            message = "Data received",
            data = iAuthRepository.getLoggedInUserData()
        )
    }
}
