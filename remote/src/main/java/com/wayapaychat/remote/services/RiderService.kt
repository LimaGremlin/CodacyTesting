package com.wayapaychat.remote.services

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.TwoZeroFourRemoteModel
import io.reactivex.Single
import retrofit2.http.PATCH
import retrofit2.http.Path

interface RiderService {
    @PATCH("user/rides/{rideId}/cancel")
    suspend fun cancelRide(@Path("rideId") rideId: String): Single<BaseNetworkModel<TwoZeroFourRemoteModel>>
}
