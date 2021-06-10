package com.wayapaychat.remote.services.auth

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.GeneralResponseRemoteModel
import com.wayapaychat.remote.models.auth.LoginnRemoteModel
import io.reactivex.Single
import retrofit2.http.*

interface AuthService {

    @POST("auth/create")
    suspend fun registerPersonalUser(@Body requestBody: HashMap<String, Any>): BaseNetworkModel<GeneralResponseRemoteModel>

    @GET("auth/create-corporate")
    suspend fun registerCooperateUser(@Body requestBody: HashMap<String, String>): Single<BaseNetworkModel<GeneralResponseRemoteModel>>

    @POST("password/change/forgot/password")
    suspend fun changePassword(@Body requestBody: HashMap<String, Any>): BaseNetworkModel<GeneralResponseRemoteModel>

    @POST("auth/login")
    suspend fun login(@Body requestBody: HashMap<String, Any>): BaseNetworkModel<LoginnRemoteModel>

    @GET("auth/resend-otp/{phoneNumber}/{email}")
    suspend fun resendOtpToPhone(
        @Path("phoneNumber") phoneNumber: String,
        @Query("email") email: String
    ): BaseNetworkModel<GeneralResponseRemoteModel>

    @POST("auth/create-pin")
    suspend fun createPin(@Body requestBody: HashMap<String, Any>): BaseNetworkModel<GeneralResponseRemoteModel>

    @POST("auth/verify-otp")
    suspend fun verifyPhoneOtp(@Body requestBody: HashMap<String, Any>): BaseNetworkModel<GeneralResponseRemoteModel>

    @GET("auth/validate-pin/{userPin}")
    suspend fun verifyPin(
        @Path("userPin") userPin: String
    ): BaseNetworkModel<GeneralResponseRemoteModel>

    @GET("password/forgot/password/{email}")
    suspend fun forgotPassword(@Path("email") email: String): BaseNetworkModel<GeneralResponseRemoteModel>
}
