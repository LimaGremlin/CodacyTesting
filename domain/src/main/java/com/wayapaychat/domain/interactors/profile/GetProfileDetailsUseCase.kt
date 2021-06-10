package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsSuspendNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import javax.inject.Inject

class GetProfileDetailsUseCase @Inject constructor(
    private val profileRepository: IProfileRepository
) : UseCaseWithNoParamsSuspendNoFlow<BaseDomainModel<UserDomainModel>>() {

    override suspend fun buildUseCase(): BaseDomainModel<UserDomainModel> =
        profileRepository.getProfileDetails()

}
