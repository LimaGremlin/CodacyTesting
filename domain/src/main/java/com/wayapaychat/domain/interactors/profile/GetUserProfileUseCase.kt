package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val IProfileRepository: IProfileRepository
) : UseCaseWithParamsNoFlow<GetUserProfileUseCase.Parameter, BaseDomainModel<UserDomainModel>>() {

    data class Parameter(
        val userId: Int,
    ) {
        companion object {
            @JvmStatic
            fun make(
                userId: Int,
            ): Parameter =
                Parameter(
                    userId,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<UserDomainModel> =
        param?.let {
            IProfileRepository.getProfileDetails()
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
