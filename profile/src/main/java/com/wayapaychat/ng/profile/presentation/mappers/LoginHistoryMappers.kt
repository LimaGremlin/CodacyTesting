package com.wayapaychat.ng.profile.presentation.mappers

import com.wayapaychat.domain.models.auth.GetLoginHistoryResponseDomainModel
import com.wayapaychat.domain.models.auth.LoginHistoryDomainModel
import com.wayapaychat.ng.profile.presentation.model.auth.GetLoginHistoryResponse
import com.wayapaychat.ng.profile.presentation.model.auth.LoginHistory

fun LoginHistoryDomainModel.toPresentation(): LoginHistory =
    LoginHistory(
        id, ip, device, city, province, country, loginDate
    )

fun GetLoginHistoryResponseDomainModel.toPresentation(): GetLoginHistoryResponse =
    GetLoginHistoryResponse(
        currentDeviceDetails.toPresentation(),
        loginHistory.map { it.toPresentation() }
    )