package com.wayapaychat.repository.mappers.payment

import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.repository.models.payment.UserBankCardDataEntity

fun UserBankCardDataEntity.toUserBankCard(): UserBankCard = UserBankCard(
    reference,
    authCode,
    accountName,
    expiryMonth,
    walletFunded,
    walletAccountNumber,
    expiryYear,
    type,
    last4digit,
    cardNumber
)
