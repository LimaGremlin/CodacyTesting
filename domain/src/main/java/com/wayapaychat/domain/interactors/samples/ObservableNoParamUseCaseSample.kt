package com.wayapaychat.domain.interactors.samples

import com.wayapaychat.domain.interactors.UseCaseWithNoParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.sampleModels.CancelReasons
import com.wayapaychat.domain.repository.CancelReasonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by edetebenezer on 2019-09-09
 **/

class ObservableNoParamUseCaseSample @Inject constructor(
    private val cancelReasonsRepository: CancelReasonsRepository
) : UseCaseWithNoParams<BaseDomainModel<CancelReasons>>() {

    override suspend fun buildUseCase(): Flow<BaseDomainModel<CancelReasons>> =
        cancelReasonsRepository.getCancelReasons()
}
