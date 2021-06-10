package com.wayapaychat.repository.remote.sample

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.sampleModels.CancelReasonsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by edetebenezer on 2019-09-09
 **/
interface CancelReasonsRemote {
    suspend fun getCancelReasons(): Flow<BaseRepositoryModel<CancelReasonsEntity>>
}
