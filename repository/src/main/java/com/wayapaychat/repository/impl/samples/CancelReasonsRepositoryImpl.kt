package com.wayapaychat.repository.impl.samples

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.sampleModels.CancelReasons
import com.wayapaychat.domain.repository.CancelReasonsRepository
import com.wayapaychat.repository.mappers.samples.CancelReasonsEntityMapper
import com.wayapaychat.repository.remote.sample.CancelReasonsRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by edetebenezer on 2019-09-09
 **/
//class CancelReasonsRepositoryImpl @Inject constructor(
class CancelReasonsRepositoryImpl(
    private val cancelReasonsRemote: CancelReasonsRemote,
    private val cancelReasonsEntityMapper: CancelReasonsEntityMapper
) : CancelReasonsRepository {
    override suspend fun getCancelReasons(): Flow<BaseDomainModel<CancelReasons>> =
        cancelReasonsRemote.getCancelReasons().map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data.let { cancelReasonsEntity ->
                    cancelReasonsEntityMapper.mapToDomain(cancelReasonsEntity!!)
                }
            )
        }
}
