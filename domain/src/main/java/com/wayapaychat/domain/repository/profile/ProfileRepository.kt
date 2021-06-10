package com.wayapaychat.domain.repository.profile

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.profile.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Flow<BaseDomainModel<UserProfileModel>>

    suspend fun updateUserInformation(
        userId: String,
        request: HashMap<String, Any>
    ): Flow<BaseDomainModel<Boolean>>
}
