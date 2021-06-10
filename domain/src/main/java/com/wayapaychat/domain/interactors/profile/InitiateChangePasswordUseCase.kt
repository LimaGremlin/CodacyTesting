package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsSuspendNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import javax.inject.Inject

class InitiateChangePasswordUseCase @Inject constructor(
    private val profileRepository: IProfileRepository
) : UseCaseWithNoParamsSuspendNoFlow<BaseDomainModel<Nothing?>>() {

    override suspend fun buildUseCase(): BaseDomainModel<Nothing?> =
            profileRepository.initiateChangePassword()

}
