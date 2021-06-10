package com.wayapaychat.remote.impl.samples

import com.wayapaychat.remote.mappers.samples.FareCalculatorRemoteModelMapper
import com.wayapaychat.remote.services.ManualRideService
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.sampleModels.FareCalculatorEntity
import com.wayapaychat.repository.remote.sample.ManualRideRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by edetebenezer on 2019-08-09
 **/

class ManualRideRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit,
    private val fareCalculatorRemoteModelMapper: FareCalculatorRemoteModelMapper
) : ManualRideRemote {

    private val manualRideService = retrofit.create(ManualRideService::class.java)

    override suspend fun calculateFareAmount(data: HashMap<String, Any>): Flow<BaseRepositoryModel<FareCalculatorEntity>> {
        return flow {
            manualRideService.calculateFare(data).map {
                BaseRepositoryModel(
                    successful = it.success,
                    message = it.message,
                    data = it.data?.let { fareCalculatorLocalModel ->
                        fareCalculatorRemoteModelMapper.mapToRepository(fareCalculatorLocalModel)
                    }
                )
            }
        }
    }
}
