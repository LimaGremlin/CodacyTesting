package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsSuspendNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.auth.GetLoginHistoryResponseDomainModel
import com.wayapaychat.domain.repository.auth.ILoginHistoryRepository
import com.wayapaychat.domain.repository.profile.IProfileRepository
import javax.inject.Inject

class GetLoginHistoryUseCase @Inject constructor(
    private val loginHistoryRepository: ILoginHistoryRepository
) : UseCaseWithNoParamsSuspendNoFlow<BaseDomainModel<GetLoginHistoryResponseDomainModel>>() {

    override suspend fun buildUseCase(): BaseDomainModel<GetLoginHistoryResponseDomainModel> =
        loginHistoryRepository.getLoginHistory()

}
