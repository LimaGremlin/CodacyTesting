package com.wayapaychat.ng.payment.mappers

import com.wayapaychat.domain.models.wallet.WalletDomainModel
import com.wayapaychat.ng.payment.model.Wallet

fun WalletDomainModel.toPresentation(): Wallet =
    Wallet(
        id, accountNo, accountName, balance, accountType, default
    )