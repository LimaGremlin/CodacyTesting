package com.wayapaychat.remote.mappers.auth

import com.wayapaychat.remote.models.auth.LoginHistoryRemoteModel
import com.wayapaychat.repository.models.auth.LoginHistoryEntity

fun LoginHistoryRemoteModel.toEntity(): LoginHistoryEntity =
    LoginHistoryEntity(
        id, ip, device, city, province, country, loginDate
    )