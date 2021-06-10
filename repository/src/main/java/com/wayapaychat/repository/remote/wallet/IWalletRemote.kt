package com.wayapaychat.repository.remote.wallet

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.wallet.WalletEntity

interface IWalletRemote {

    suspend fun getWallets(userId: Int): BaseRepositoryModel<List<WalletEntity>>

    suspend fun createWallet(userId: Int): BaseRepositoryModel<WalletEntity>

    suspend fun setDefaultWallet(userId: Int, accountNo: String): BaseRepositoryModel<Nothing>

    suspend fun performWalletToWalletTransaction(request: HashMap<String, Any>): BaseRepositoryModel<Nothing>

}