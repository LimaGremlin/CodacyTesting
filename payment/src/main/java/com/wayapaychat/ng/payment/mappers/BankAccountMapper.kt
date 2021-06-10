package com.wayapaychat.ng.payment.mappers

import com.wayapaychat.domain.models.payment.BankAccountDomainModel
import com.wayapaychat.ng.payment.model.BankAccount

fun BankAccountDomainModel.toPresentation(): BankAccount =
    BankAccount(
        accountName, accountNumber, bankName, bankCode
    )