package com.wayapaychat.remote.impl.samples

import com.wayapaychat.remote.mappers.samples.CancelReasonsLocalModelMapper
import com.wayapaychat.remote.services.CancelReasonsService
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.sampleModels.CancelReasonsEntity
import com.wayapaychat.repository.remote.sample.CancelReasonsRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by edetebenezer on 2019-09-09
 **/
class CancelReasonRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit,
    private val cancelReasonsLocalModelMapper: CancelReasonsLocalModelMapper
) : CancelReasonsRemote {

    private val cancelReasonService = retrofit.create(CancelReasonsService::class.java)

    override suspend fun getCancelReasons(): Flow<BaseRepositoryModel<CancelReasonsEntity>> {
        return flow {
            cancelReasonService.getCancelReasons().map {
                BaseRepositoryModel(
                    successful = it.success,
                    message = it.message,
                    data = it.data.let { cancelReasons ->
                        cancelReasonsLocalModelMapper.mapToRepository(cancelReasons!!)
                    }
                )
            }
        }
    }
}
