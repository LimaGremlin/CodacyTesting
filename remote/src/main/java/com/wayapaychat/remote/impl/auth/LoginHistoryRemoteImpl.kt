package com.wayapaychat.remote.impl.auth

import com.wayapaychat.remote.mappers.auth.toEntity
import com.wayapaychat.remote.services.auth.LoginHistoryService
import com.wayapaychat.remote.utils.WayaAppExceptionHandler
import com.wayapaychat.remote.utils.WayaAppExceptionHandler.getErrorFromThrowable
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.auth.LoginHistoryEntity
import com.wayapaychat.repository.remote.auth.ILoginHistoryRemote
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class LoginHistoryRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit
): ILoginHistoryRemote {

    private val loginHistoryService = retrofit.create(LoginHistoryService::class.java)

    override suspend fun saveLoginHistory(request: HashMap<String, Any>): BaseRepositoryModel<LoginHistoryEntity> =
        try {
            val response = loginHistoryService.saveLoginHistory(request)
            BaseRepositoryModel(
                response.success,
                response.data?.data?.toEntity(),
                response.message
            )
        }catch (throwable: Throwable){
            BaseRepositoryModel(
                false,
                null,
                getErrorFromThrowable(throwable)
            )
        }

    override suspend fun getLoginHistory(): BaseRepositoryModel<List<LoginHistoryEntity>> =
        try {
            val response = loginHistoryService.getLoginHistory()
            println("Was successful  ${response.success}")
            BaseRepositoryModel(
                response.success,
                response.data?.data?.map { it.toEntity() },
                response.message
            )
        }catch (throwable: Throwable){
            BaseRepositoryModel(
                false,
                null,
                getErrorFromThrowable(throwable)
            )
        }

}