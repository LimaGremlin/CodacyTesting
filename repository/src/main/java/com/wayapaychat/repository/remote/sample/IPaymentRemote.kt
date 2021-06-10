package com.wayapaychat.repository.remote.sample

import com.wayapaychat.repository.models.BaseRepositoryModel
import kotlinx.coroutines.flow.Flow

interface IPaymentRemote {
    suspend fun deleteCard(data: HashMap<String, String>): Flow<BaseRepositoryModel<String>>
}
