package com.wayapaychat.repository.mappers.payment

import com.wayapaychat.domain.models.payment.BankAccountDomainModel
import com.wayapaychat.repository.models.payment.BankAccountEntity

fun BankAccountEntity.toDomain(): BankAccountDomainModel =
    BankAccountDomainModel(
        accountName, accountNumber, bankName, bankCode
    )