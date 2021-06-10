package com.wayapaychat.remote.services.wallet

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.GenericResponseRemoteModel
import com.wayapaychat.remote.models.wallet.CreateWalletRequestRemoteModel
import com.wayapaychat.remote.models.wallet.WalletRemoteModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WalletService{

    @GET("wallet/accounts/{user_id}")
    suspend fun getWallets(@Path("user_id") userId: Int)
            : BaseNetworkModel<GenericResponseRemoteModel<List<WalletRemoteModel>>>

    @POST("wallet/create-wallet")
    suspend fun createWallet(@Body request: CreateWalletRequestRemoteModel)
            : BaseNetworkModel<GenericResponseRemoteModel<WalletRemoteModel>>

    @GET("wallet/set-default-account/{user_id}/{account_no}")
    suspend fun setDefaultWallet(
        @Path("user_id") userId: Int,
        @Path("account_no") accountNo: String
    ): BaseNetworkModel<GenericResponseRemoteModel<Nothing>>

    @POST("wallet/wallet2wallet")
    suspend fun performWalletToWalletTransaction(
        @Body request: HashMap<String, Any>
    ): BaseNetworkModel<GenericResponseRemoteModel<Nothing>>

}