package com.wayapaychat.domain.repository

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.sampleModels.FareCalculator
import kotlinx.coroutines.flow.Flow

/**
 * Created by edetebenezer on 2019-08-09
 **/

interface IDriverDetailsRepository {
    suspend fun calculateFare(data: HashMap<String, Any>): Flow<BaseDomainModel<FareCalculator>>
}
