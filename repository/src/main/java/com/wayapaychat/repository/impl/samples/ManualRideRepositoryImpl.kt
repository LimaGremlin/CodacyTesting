package com.wayapaychat.repository.impl.samples

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.sampleModels.FareCalculator
import com.wayapaychat.domain.repository.IDriverDetailsRepository
import com.wayapaychat.repository.mappers.samples.FareCalculatorEntityMapper
import com.wayapaychat.repository.remote.sample.ManualRideRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by edetebenezer on 2019-08-11
 **/
//class ManualRideRepositoryImpl @Inject constructor(
class ManualRideRepositoryImpl(
    private val manualRideDetailsRemote: ManualRideRemote,
    private val fareCalculatorEntityMapper: FareCalculatorEntityMapper
) : IDriverDetailsRepository {
    override suspend fun calculateFare(data: HashMap<String, Any>): Flow<BaseDomainModel<FareCalculator>> =
        manualRideDetailsRemote.calculateFareAmount(data).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data?.let { fareCalculator ->
                    fareCalculatorEntityMapper.mapToDomain(fareCalculator)
                }
            )
        }
}
