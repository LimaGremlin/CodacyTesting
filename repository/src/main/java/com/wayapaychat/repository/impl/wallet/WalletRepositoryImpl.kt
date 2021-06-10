package com.wayapaychat.repository.impl.wallet

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.wallet.WalletDomainModel
import com.wayapaychat.domain.repository.wallet.IWalletRepository
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.mappers.wallet.toDomain
import com.wayapaychat.repository.remote.wallet.IWalletRemote

class WalletRepositoryImpl(
    private val iAuthenticationLocal: IAuthenticationLocal,
    private val walletRemote: IWalletRemote
): IWalletRepository {

    override suspend fun getWallets(): BaseDomainModel<List<WalletDomainModel>> {
        val userData = iAuthenticationLocal.getSavedUserData()
        val response = walletRemote.getWallets(userData.user.id)
        return BaseDomainModel(
            response.successful,
            response.data?.map { it.toDomain() },
            response.message
        )
    }

    override suspend fun createWallet(): BaseDomainModel<WalletDomainModel> {
        val userId = iAuthenticationLocal.getSavedUserData().user.id
        val response = walletRemote.createWallet(userId)
        return BaseDomainModel(
            response.successful,
            response.data?.toDomain(),
            response.message
        )
    }

    override suspend fun setDefaultWallet(accountNo: String): BaseDomainModel<Nothing> {
        val userId = iAuthenticationLocal.getSavedUserData().user.id
        val response = walletRemote.setDefaultWallet(userId, accountNo)
        return BaseDomainModel(
            response.successful,
            null,
            response.message
        )
    }

    override suspend fun performWalletToWalletTransaction(request: HashMap<String, Any>): BaseDomainModel<Nothing> {
        val response = walletRemote.performWalletToWalletTransaction(request)
        return BaseDomainModel(
            response.successful,
            null,
            response.message
        )
    }

}