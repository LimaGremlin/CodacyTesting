package com.wayapaychat.remote.mappers.payment

import com.wayapaychat.remote.models.payment.BankRemoteModel
import com.wayapaychat.repository.models.payment.BankEntity

fun BankRemoteModel.toEntity(): BankEntity =
    BankEntity(
        name, code
    )