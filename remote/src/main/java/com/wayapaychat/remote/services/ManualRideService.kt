package com.wayapaychat.remote.services

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.FareCalculatorLocalModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by edetebenezer on 2019-08-09
 **/

interface ManualRideService {
    @POST("fare/calculation")
    suspend fun calculateFare(@Body data: HashMap<String, Any>): Single<BaseNetworkModel<FareCalculatorLocalModel>>
}
