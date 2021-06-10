package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UpdateProfilePictureUseCase @Inject constructor(
    private val profileRepository: IProfileRepository
) : UseCaseWithParamsNoFlow<File, BaseDomainModel<String>>() {

    override suspend fun buildUseCase(param: File?): BaseDomainModel<String> =
        param?.let {
            profileRepository.updateProfilePicture(param)
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
