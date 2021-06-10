package com.wayapaychat.remote.services

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.auth.CancelReasonsLocalModel
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by edetebenezer on 2019-09-09
 **/
interface CancelReasonsService {
    @GET("user/cancelReasons")
    suspend fun getCancelReasons(): Single<BaseNetworkModel<CancelReasonsLocalModel>>
}
