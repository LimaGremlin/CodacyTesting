package com.wayapay.ng.landingpage.network

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wayapaychat.core.newtwork.LogInDetails
import com.wayapaychat.core.newtwork.PersonalUser
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val WAYA_PAY_BASE_URL = "http://68.183.60.114:8059/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * We use a loggin interceptor to catch error from server
 */
private var clientBuilder = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))


/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(clientBuilder.build())
    .baseUrl(WAYA_PAY_BASE_URL)
    .build()

interface WayaPayApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @POST("auth/login")
    fun logInAsync(@Body params: LogInDetails): Deferred<Response<JsonObject>>

    @GET("auth/resend-otp-mail/{email}/{userId}")
    fun resendVerificationEmailAsync(
        @Path("email") email: String,
        @Path("userId") userId: String
    ): Deferred<String>

    @GET("auth/resend-otp/{phoneNumber}/{email}")
    fun resendOTPAsync(
        @Path("phoneNumber") phoneNumber: String,
        @Path("email") email: String
    ): Deferred<String>

    @GET("user/email/{email}")
    fun getUserByEmailAsync(@Path("email") email: String): Deferred<JsonObject>

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object WayaPaySocialsApi{
    val retrofitService: WayaPayApiServices by lazy {
        retrofit.create(WayaPayApiServices::class.java)
    }
}