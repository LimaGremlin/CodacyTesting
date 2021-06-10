package com.wayapaychat.remote.mappers.payment

import com.wayapaychat.remote.models.payment.ResolveAccountNumberResponseRemoteModel
import com.wayapaychat.repository.models.payment.ResolveAccountNumberResponseEntity

fun ResolveAccountNumberResponseRemoteModel.toEntity(): ResolveAccountNumberResponseEntity =
    ResolveAccountNumberResponseEntity(
        accountNumber, accountName
    )