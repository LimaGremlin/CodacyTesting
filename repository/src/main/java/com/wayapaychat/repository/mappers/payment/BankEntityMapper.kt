package com.wayapaychat.repository.mappers.payment

import com.wayapaychat.domain.models.payment.BankDomainModel
import com.wayapaychat.repository.models.payment.BankEntity

fun BankEntity.toDomain(): BankDomainModel =
    BankDomainModel(
        name, code
    )