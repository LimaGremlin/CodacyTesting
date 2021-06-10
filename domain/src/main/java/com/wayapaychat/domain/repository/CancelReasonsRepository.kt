package com.wayapaychat.domain.repository

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.sampleModels.CancelReasons
import kotlinx.coroutines.flow.Flow

/**
 * Created by edetebenezer on 2019-09-09
 **/
interface CancelReasonsRepository {
    suspend fun getCancelReasons(): Flow<BaseDomainModel<CancelReasons>>
}
