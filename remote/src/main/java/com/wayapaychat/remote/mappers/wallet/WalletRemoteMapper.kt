package com.wayapaychat.remote.mappers.wallet

import com.wayapaychat.remote.models.wallet.WalletRemoteModel
import com.wayapaychat.repository.models.wallet.WalletEntity

fun WalletRemoteModel.toEntity(): WalletEntity =
    WalletEntity(
        id, accountNo, accountName, balance, accountType, default
    )