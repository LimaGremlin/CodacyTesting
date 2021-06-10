package com.wayapaychat.remote.mappers.payment

import com.wayapaychat.remote.models.payment.AddCardDataModel
import com.wayapaychat.remote.utils.safeString
import com.wayapaychat.repository.models.payment.AddCardEntity

fun AddCardDataModel.toAddCardEntity(): AddCardEntity =
    AddCardEntity(
        reference.safeString(),
        displayText.safeString(),
        status.safeString()
    )
