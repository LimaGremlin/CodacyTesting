package com.wayapaychat.remote.services.auth

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.GeneralResponseRemoteModel
import com.wayapaychat.remote.models.GenericResponseRemoteModel
import com.wayapaychat.remote.models.auth.LoginHistoryRemoteModel
import com.wayapaychat.repository.models.BaseRepositoryModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginHistoryService {

    @POST("history/save")
    suspend fun saveLoginHistory(@Body request: HashMap<String, Any>): BaseNetworkModel<GenericResponseRemoteModel<LoginHistoryRemoteModel>>

    @GET("history/my-history")
    suspend fun getLoginHistory(): BaseNetworkModel<GenericResponseRemoteModel<List<LoginHistoryRemoteModel>>>

}