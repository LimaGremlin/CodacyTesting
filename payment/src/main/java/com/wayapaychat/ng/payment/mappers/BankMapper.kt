package com.wayapaychat.ng.payment.mappers

import com.wayapaychat.domain.models.payment.BankDomainModel
import com.wayapaychat.ng.payment.model.Bank

fun BankDomainModel.toPresentation(): Bank =
    Bank(
        name, code
    )