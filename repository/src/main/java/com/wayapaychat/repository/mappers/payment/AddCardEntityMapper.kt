package com.wayapaychat.repository.mappers.payment

import com.wayapaychat.domain.models.payment.AddCard
import com.wayapaychat.repository.models.payment.AddCardEntity

fun AddCardEntity.toAddCard(): AddCard =
    AddCard(
        reference,
        displayText,
        status
    )
