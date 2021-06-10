package com.wayapaychat.remote.mappers.payment

import com.wayapaychat.remote.models.payment.UserBankCardDataRemoteModel
import com.wayapaychat.remote.utils.safeBoolean
import com.wayapaychat.remote.utils.safeString
import com.wayapaychat.repository.models.payment.UserBankCardDataEntity

fun UserBankCardDataRemoteModel.toUserBankCardDataEntity(): UserBankCardDataEntity =
    UserBankCardDataEntity(
        reference.safeString(),
        authCode,
        accountName.safeString(),
        expiryMonth.safeString(),
        walletFunded.safeBoolean(),
        walletAccountNumber.safeString(),
        expiryYear.safeString(),
        type.safeString(),
        last4digit.safeString(),
        cardNumber.safeString(),
    )
