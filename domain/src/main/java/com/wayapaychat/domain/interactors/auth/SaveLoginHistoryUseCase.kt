package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.ILoginHistoryRepository
import com.wayapaychat.domain.repository.profile.IProfileRepository
import javax.inject.Inject

class SaveLoginHistoryUseCase @Inject constructor(
    private val loginHistoryRepository: ILoginHistoryRepository
) : UseCaseWithParamsNoFlow<SaveLoginHistoryUseCase.Parameter, BaseDomainModel<Nothing>>() {

    data class Parameter(
        val city: String,
        val country: String,
        val device: String,
        val ip: String,
        val province: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                city: String,
                country: String,
                device: String,
                ip: String,
                province: String
            ): Parameter =
                Parameter(
                    city, country, device, ip, province
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Nothing> =
        param?.let {
            loginHistoryRepository.saveLoginHistory(
                hashMapOf(
                    "city" to it.city,
                    "country" to it.country,
                    "device" to it.device,
                    "ip" to it.ip,
                    "province" to it.province
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
