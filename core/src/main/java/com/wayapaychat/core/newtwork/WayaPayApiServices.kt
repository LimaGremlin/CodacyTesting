package com.wayapaychat.core.newtwork

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
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


    @POST("auth/create")
    fun personalUserRegistrationAsync(@Body params: PersonalUser): Deferred<Response<JsonObject>>

    @FormUrlEncoded
    @POST("auth/create-corporate")
    fun cooperateUserRegistrationAsync(@FieldMap params: Map<String, String>): Deferred<Response<JsonObject>>

    //Create Pin
    @POST("auth/create-pin")
    fun createPinAsync(@Body params: CreatePin,
                       @Header("Authorization") authHeader: String): Deferred<Response<JsonObject>>

    @POST("auth/login")
    fun logInAsync(@Body params: Map<String, String>): Deferred<Response<JsonObject>>

    @FormUrlEncoded
    @POST("auth/verify-email")
    fun verifyEmailAsync(@FieldMap params: Map<String, String>): Deferred<Response<JsonObject>>

    //Verify OTP
    @POST("auth/verify-otp")
    fun verifyOTPAsync(@Body params: Map<String, String>): Deferred<Response<JsonObject>>

    @GET("auth/validate-pin/{pin}")
    fun validatePinAsync(@Path("pin") pin: String,
                         @Header("authorization") authHeader: String): Deferred<Response<JsonObject>>

    @GET("auth/resend-otp/{phoneNumber}/{email}")
    fun resendOTPAsync(@Path("phoneNumber") phoneNumber:String,
                                     @Path("email") email:String): Deferred<Response<JsonObject>>



}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object WayaPayApi{
    val retrofitService: WayaPayApiServices by lazy {
        retrofit.create(WayaPayApiServices::class.java)
    }
}