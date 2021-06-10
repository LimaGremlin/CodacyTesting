package com.wayapaychat.remote.impl.samples

import com.wayapaychat.remote.services.PaymentService
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.remote.sample.IPaymentRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class PaymentRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) paymentRetrofit: Retrofit
) : IPaymentRemote {

    private val paymentService = paymentRetrofit.create(PaymentService::class.java)

    override suspend fun deleteCard(data: HashMap<String, String>): Flow<BaseRepositoryModel<String>> {
        return flow {
            paymentService.deleteCard(data).map {
                BaseRepositoryModel(
                    successful = it.success,
                    message = it.message,
                    data = it.data?.response?.message
                )
            }
        }
    }
}
