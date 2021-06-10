package com.wayapaychat.repository.remote.profile

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.profile.UserProfileEntityModel
import kotlinx.coroutines.flow.Flow

interface ProfileRemote {
    suspend fun getUserProfile(userId: String): Flow<BaseRepositoryModel<UserProfileEntityModel>>

    suspend fun updateUserInformation(userId: String, request: HashMap<String, Any>): Flow<BaseRepositoryModel<Boolean>>
}
