package com.wayapaychat.domain.repository.auth

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.auth.GetLoginHistoryResponseDomainModel
import com.wayapaychat.domain.models.auth.LoginHistoryDomainModel

interface ILoginHistoryRepository{

    suspend fun saveLoginHistory(request: HashMap<String, Any>): BaseDomainModel<Nothing>

    suspend fun getLoginHistory(): BaseDomainModel<GetLoginHistoryResponseDomainModel>

}