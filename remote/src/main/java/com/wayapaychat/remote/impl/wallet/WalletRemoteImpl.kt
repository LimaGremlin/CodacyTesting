package com.wayapaychat.remote.impl.wallet

import com.wayapaychat.remote.mappers.wallet.toEntity
import com.wayapaychat.remote.models.wallet.CreateWalletRequestRemoteModel
import com.wayapaychat.remote.models.wallet.WalletRemoteModel
import com.wayapaychat.remote.services.wallet.WalletService
import com.wayapaychat.remote.utils.WayaAppExceptionHandler
import com.wayapaychat.remote.utils.WayaAppExceptionHandler.getErrorFromThrowable
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.wallet.WalletEntity
import com.wayapaychat.repository.remote.wallet.IWalletRemote
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class WalletRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.WALLET_RETROFIT) retrofit: Retrofit
) : IWalletRemote {

    private val walletService = retrofit.create(WalletService::class.java)

    override suspend fun getWallets(userId: Int): BaseRepositoryModel<List<WalletEntity>> {
        return try{
            val response = walletService.getWallets(userId)
            BaseRepositoryModel(
                response.success,
                response.data?.data?.map { it.toEntity() },
                response.message
            )
        }catch (throwable: Throwable){
            BaseRepositoryModel(
                false,
                null,
                getErrorFromThrowable(throwable)
            )
        }
    }

    override suspend fun createWallet(userId: Int): BaseRepositoryModel<WalletEntity> =
        try{
            val response =
                walletService.createWallet(
                    CreateWalletRequestRemoteModel(userId)
                )
            BaseRepositoryModel(
                response.success,
                response.data?.data?.toEntity(),
                response.message
            )
        }catch (throwable: Throwable){
            BaseRepositoryModel(
                false,
                null,
                getErrorFromThrowable(throwable)
            )
        }

    override suspend fun setDefaultWallet(
        userId: Int,
        accountNo: String
    ): BaseRepositoryModel<Nothing> =
        try{
            val response =
                walletService.setDefaultWallet(
                    userId, accountNo
                )
            BaseRepositoryModel(
                response.success,
                null,
                response.message
            )
        }catch (throwable: Throwable){
            BaseRepositoryModel(
                false,
                null,
                getErrorFromThrowable(throwable)
            )
        }

    override suspend fun performWalletToWalletTransaction(request: HashMap<String, Any>): BaseRepositoryModel<Nothing> =
        try{
            val response =
                walletService.performWalletToWalletTransaction(request)
            BaseRepositoryModel(
                response.success,
                null,
                response.message
            )
        }catch (throwable: Throwable){
            BaseRepositoryModel(
                false,
                null,
                getErrorFromThrowable(throwable)
            )
        }

}