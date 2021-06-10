package com.wayapaychat.repository.mappers.auth

import com.wayapaychat.domain.models.auth.LoginHistoryDomainModel
import com.wayapaychat.repository.models.auth.LoginHistoryEntity

fun LoginHistoryEntity.toDomain(): LoginHistoryDomainModel =
    LoginHistoryDomainModel(
        id, ip, device, city, province, country, loginDate
    )