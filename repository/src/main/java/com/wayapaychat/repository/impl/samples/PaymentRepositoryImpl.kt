package com.wayapaychat.repository.impl.samples

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.IPaymentRepository
import com.wayapaychat.repository.remote.sample.IPaymentRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//class PaymentRepositoryImpl @Inject constructor(
class PaymentRepositoryImpl(
    private val paymentRemote: IPaymentRemote
) : IPaymentRepository {
    override suspend fun deleteCard(data: HashMap<String, String>): Flow<BaseDomainModel<String>> =
        paymentRemote.deleteCard(data).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }
}
