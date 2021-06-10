package com.wayapaychat.repository.remote.auth

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.auth.LoginHistoryEntity

interface ILoginHistoryRemote {

    suspend fun saveLoginHistory(request: HashMap<String, Any>): BaseRepositoryModel<LoginHistoryEntity>

    suspend fun getLoginHistory(): BaseRepositoryModel<List<LoginHistoryEntity>>

}