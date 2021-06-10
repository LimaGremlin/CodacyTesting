package com.wayapaychat.repository.remote.sample

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.sampleModels.FareCalculatorEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by edetebenezer on 2019-08-09
 **/
interface ManualRideRemote {
    suspend fun calculateFareAmount(data: HashMap<String, Any>): Flow<BaseRepositoryModel<FareCalculatorEntity>>
}
