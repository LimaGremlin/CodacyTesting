package com.wayapaychat.remote.mappers.payment

import com.wayapaychat.remote.models.payment.BankAccountRemoteModel
import com.wayapaychat.repository.models.payment.BankAccountEntity

fun BankAccountRemoteModel.toEntity(): BankAccountEntity =
    BankAccountEntity(
        accountName, accountNumber, bankName, bankCode
    )