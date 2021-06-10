package com.wayapaychat.repository.impl.auth

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.auth.GetLoginHistoryResponseDomainModel
import com.wayapaychat.domain.models.auth.LoginHistoryDomainModel
import com.wayapaychat.domain.repository.auth.ILoginHistoryRepository
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import com.wayapaychat.repository.mappers.auth.toDomain
import com.wayapaychat.repository.models.auth.LoginHistoryEntity
import com.wayapaychat.repository.remote.auth.ILoginHistoryRemote
import javax.inject.Inject

class LoginHistoryRepositoryImpl @Inject constructor(
    private val iAuthenticationLocal: IAuthenticationLocal,
    private val loginHistoryRemote: ILoginHistoryRemote,
    private val iWayaPreferenceRepository: IWayaPreferenceRepository
): ILoginHistoryRepository {

    override suspend fun saveLoginHistory(request: HashMap<String, Any>): BaseDomainModel<Nothing> {
        val userData = iAuthenticationLocal.getSavedUserData()
        request["userId"] = userData.user.id
        val response = loginHistoryRemote.saveLoginHistory(request)
        if (response.successful){
            iWayaPreferenceRepository.saveLoginHistoryId(response.data?.id ?: 0)
        }
        return BaseDomainModel(
            response.successful,
            null,
            response.message
        )
    }

    override suspend fun getLoginHistory(): BaseDomainModel<GetLoginHistoryResponseDomainModel> {
        val currentDeviceLoginHistoryId = iWayaPreferenceRepository.getLoginHistoryId()
        val response = loginHistoryRemote.getLoginHistory()

        val data =
            if (response.successful){
                val loginHistory = response.data?.toMutableList() ?: arrayListOf()
                val currentDeviceLoginHistory = loginHistory.find { it.id == currentDeviceLoginHistoryId }
                loginHistory.remove(currentDeviceLoginHistory)
                GetLoginHistoryResponseDomainModel(
                    currentDeviceLoginHistory?.toDomain()
                        ?: LoginHistoryDomainModel(
                            0, "", "", "", "", "", ""
                        ),
                    loginHistory.map { it.toDomain() }
                )
            }else{
                null
            }

        return BaseDomainModel(
            response.successful,
            data,
            response.message
        )
    }

}