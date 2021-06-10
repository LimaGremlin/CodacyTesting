package com.wayapaychat.repository.mappers.samples

import com.wayapaychat.domain.models.sampleModels.FareCalculator
import com.wayapaychat.repository.mappers.EntityMapper
import com.wayapaychat.repository.models.sampleModels.FareCalculatorEntity
import javax.inject.Inject

/**
 * Created by edetebenezer on 2019-08-22
 **/
class FareCalculatorEntityMapper @Inject constructor(
): EntityMapper<FareCalculatorEntity, FareCalculator> {
    override fun mapToDomain(entity: FareCalculatorEntity): FareCalculator =
        FareCalculator(
            duration = entity.duration,
            totalTo = entity.totalTo,
            totalFrom = entity.totalFrom,
            distance = entity.distance,
            codedCoordinates = entity.codedCoordinates,
            perDuration = entity.perDuration,
            perDistance = entity.perDistance,
            base = entity.base
        )
}
