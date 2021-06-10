package com.wayapaychat.repository.mappers.payment

import com.wayapaychat.domain.models.payment.ResolveAccountNumberResponseDomainModel
import com.wayapaychat.repository.models.payment.ResolveAccountNumberResponseEntity

fun ResolveAccountNumberResponseEntity.toDomain(): ResolveAccountNumberResponseDomainModel =
    ResolveAccountNumberResponseDomainModel(
        accountNumber, accountName
    )