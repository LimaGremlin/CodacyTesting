package com.wayapaychat.domain.repository.wallet

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.wallet.WalletDomainModel

interface IWalletRepository {

    suspend fun getWallets(): BaseDomainModel<List<WalletDomainModel>>

    suspend fun createWallet(): BaseDomainModel<WalletDomainModel>

    suspend fun setDefaultWallet(accountNo: String): BaseDomainModel<Nothing>

    suspend fun performWalletToWalletTransaction(request: HashMap<String, Any>): BaseDomainModel<Nothing>

}