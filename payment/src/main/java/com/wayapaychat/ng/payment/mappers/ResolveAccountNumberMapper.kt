package com.wayapaychat.ng.payment.mappers

import com.wayapaychat.domain.models.payment.ResolveAccountNumberResponseDomainModel
import com.wayapaychat.ng.payment.model.ResolveAccountNumberResponse

fun ResolveAccountNumberResponseDomainModel.toPresentation(): ResolveAccountNumberResponse =
    ResolveAccountNumberResponse(
        accountNumber, accountName
    )