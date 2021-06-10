package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val IProfileRepository: IProfileRepository
) : UseCaseWithParamsNoFlow<ChangePasswordUseCase.Parameter, BaseDomainModel<Nothing?>>() {

    data class Parameter(
        val newPassword: String,
        val oldPassword: String,
        val otp: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                newPassword: String,
                oldPassword: String,
                otp: String
            ): Parameter =
                Parameter(
                    newPassword,
                    oldPassword,
                    otp
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Nothing?> =
        param?.let {
            IProfileRepository.changePassword(
                hashMapOf(
                    "newPassword" to it.newPassword,
                    "oldPassword" to it.oldPassword,
                    "otp" to it.otp
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
