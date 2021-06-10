package com.wayapaychat.remote.services.payment

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.GeneralResponseRemoteModel
import com.wayapaychat.remote.models.GenericResponseRemoteModel
import com.wayapaychat.remote.models.payment.*
import retrofit2.http.*
import com.wayapaychat.remote.models.payment.AddCardRemoteModel
import com.wayapaychat.remote.models.payment.BankRemoteModel
import com.wayapaychat.remote.models.payment.ResolveAccountNumberResponseRemoteModel
import com.wayapaychat.remote.models.payment.UserBankCardsRemoteModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CardService {

    @GET("api/card/list")
    suspend fun getUserBankCards(): BaseNetworkModel<UserBankCardsRemoteModel>

    @POST("api/card/add")
    suspend fun addBankCard(
        @Body requestBody: HashMap<String, Any>
    ): BaseNetworkModel<AddCardRemoteModel>

    @DELETE("api/card/delete/{cardNumber}")
    suspend fun deleteBankCard(@Path("cardNumber") cardNumber: String): BaseNetworkModel<GeneralResponseRemoteModel>

    @POST("api/bankAccount/add")
    suspend fun addBankAccount(@Body request: HashMap<String, String>): BaseNetworkModel<GenericResponseRemoteModel<Nothing>>

    @GET("api/bankAccount/getBanks")
    suspend fun getBanks(): BaseNetworkModel<GenericResponseRemoteModel<List<BankRemoteModel>>>

    @POST("api/bankAccount/resolveAccountNumber")
    suspend fun resolveAccountNumber(@Body request: HashMap<String, String>)
        : BaseNetworkModel<GenericResponseRemoteModel<ResolveAccountNumberResponseRemoteModel>>

    @POST("api/card/submitOtp")
    suspend fun submitOTP(
        @Body requestBody: HashMap<String, Any>
    ): BaseNetworkModel<GeneralResponseRemoteModel>

    @GET("api/bankAccount/list")
    suspend fun getBankAccounts(): BaseNetworkModel<GenericResponseRemoteModel<List<BankAccountRemoteModel>>>

    @DELETE("api/bankAccount/delete/{account_number}")
    suspend fun deleteBankAccount(@Path("account_number") accountNumber: String)
            : BaseNetworkModel<GenericResponseRemoteModel<Nothing>>

    @FormUrlEncoded
    @POST("api/card/submitOtp")
    suspend fun submitOTP(
        @Field("otp") otp: String,
        @Field("reference") reference: String,
    ): BaseNetworkModel<GeneralResponseRemoteModel>

    @FormUrlEncoded
    @POST("api/card/submitPhone")
    suspend fun submitPhoneNumber(
        @Field("phone") phoneNumber: String,
        @Field("reference") reference: String,
    ): BaseNetworkModel<AddCardRemoteModel>
}
