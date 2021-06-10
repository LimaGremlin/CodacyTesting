package com.wayapaychat.repository.impl.profile

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.profile.UserProfileModel
import com.wayapaychat.domain.repository.profile.ProfileRepository
import com.wayapaychat.repository.mappers.profile.toUserProfileModel
import com.wayapaychat.repository.remote.profile.ProfileRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IProfileRepositoryImpl(
    private val profileRemote: ProfileRemote,
) : ProfileRepository {

    override suspend fun getUserProfile(userId: String): Flow<BaseDomainModel<UserProfileModel>> =
        profileRemote.getUserProfile(userId).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data?.toUserProfileModel()
            )
        }

    override suspend fun updateUserInformation(
        userId: String,
        request: HashMap<String, Any>
    ): Flow<BaseDomainModel<Boolean>> =
        profileRemote.updateUserInformation(userId, request).map {
            BaseDomainModel(
                successful = it.successful,
                message = it.message,
                data = it.data
            )
        }
}
