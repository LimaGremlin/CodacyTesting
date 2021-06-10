package com.wayapaychat.remote.mappers.samples

import com.wayapaychat.remote.mappers.RemoteMapper
import com.wayapaychat.remote.models.ReasonsLocalModel
import com.wayapaychat.remote.models.auth.CancelReasonsLocalModel
import com.wayapaychat.repository.models.sampleModels.CancelReasonsEntity
import com.wayapaychat.repository.models.sampleModels.ReasonsEntity

/**
 * Created by edetebenezer on 2019-09-09
 **/
class CancelReasonsLocalModelMapper : RemoteMapper<CancelReasonsLocalModel, CancelReasonsEntity> {
    override fun mapToRepository(remote: CancelReasonsLocalModel): CancelReasonsEntity {
        val reasons = remote.data!!.map {
            it.toReasonsEntity()
        }
        return CancelReasonsEntity(
            data = reasons
        )
    }
}


fun ReasonsLocalModel.toReasonsEntity(): ReasonsEntity {
    return ReasonsEntity(
        createdAt = createdAt!!,
        id = id!!,
        V = V!!,
        tag = tag!!,
        type = type!!,
        body = body!!,
        updatedAt = updatedAt!!
    )
}
