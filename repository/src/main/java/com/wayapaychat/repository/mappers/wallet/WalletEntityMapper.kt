package com.wayapaychat.repository.mappers.wallet

import com.wayapaychat.domain.models.wallet.WalletDomainModel
import com.wayapaychat.repository.models.wallet.WalletEntity

fun WalletEntity.toDomain(): WalletDomainModel =
    WalletDomainModel(
        id, accountNo, accountName, balance, accountType, default
    )