package com.wayapaychat.remote.services.payment

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val ACCOUNT_BASE_URL = "http://157.230.223.54:3020/card-service/api/"

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
    .baseUrl(ACCOUNT_BASE_URL)
    .build()

interface AccountApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @GET("bankAccount/list")
    fun getAllUserBanksAsync(@Header("authorization") authHeader: String): Deferred<JsonObject>

    @GET("bankAccount/getBanks")
    fun getAllBanksAsync(@Header("authorization") authHeader: String):Deferred<JsonObject>

    @POST("bankAccount/add")
    fun addBankAccountAsync(@Body params: Map<String, String>,
                             @Header("authorization") authHeader: String): Deferred<JsonObject>

    @DELETE("bankAccount/delete/{accountNumber}")
    fun deleteBankAccountAsync(@Path("accountNumber") accountNumber: String,
                               @Header("authorization") authHeader: String) : Deferred<JsonObject>
}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object AccountApi{
    val retrofitService: AccountApiServices by lazy {
        retrofit.create(
            AccountApiServices::class.java)
    }
}