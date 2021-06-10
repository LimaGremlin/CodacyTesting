package com.wayapaychat.ng.wallet.mappers

import com.wayapaychat.domain.models.wallet.WalletDomainModel
import com.wayapaychat.ng.wallet.models.Wallet

fun WalletDomainModel.toPresentation(): Wallet =
    Wallet(
        id, accountNo, accountName, balance, accountType, default
    )