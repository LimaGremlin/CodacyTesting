package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsSuspendNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import javax.inject.Inject

class GetReferralCodeUseCase @Inject constructor(
    private val profileRepository: IProfileRepository
) : UseCaseWithNoParamsSuspendNoFlow<BaseDomainModel<String>>() {

    override suspend fun buildUseCase(): BaseDomainModel<String> =
        profileRepository.getReferralCode()

}
