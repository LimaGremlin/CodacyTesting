package com.wayapaychat.remote.services

import com.wayapaychat.remote.base.BaseDataWrapperRemote
import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.StringResponse
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.QueryMap

/**
 * Created by Isaac Iniongun on 2019-09-18.
 * For waya-app-android project.
 */

interface PaymentService {

    @DELETE("card/deactivateCustomerCard")
    suspend fun deleteCard(@QueryMap queryParameters: HashMap<String, String>): Single<BaseNetworkModel<BaseDataWrapperRemote<StringResponse>>>
}
