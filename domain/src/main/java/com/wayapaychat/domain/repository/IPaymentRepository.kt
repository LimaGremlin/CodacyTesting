package com.wayapaychat.domain.repository

import com.wayapaychat.domain.models.BaseDomainModel
import kotlinx.coroutines.flow.Flow

interface IPaymentRepository {
    suspend fun deleteCard(data: HashMap<String, String>): Flow<BaseDomainModel<String>>
}
