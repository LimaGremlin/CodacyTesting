package com.wayapaychat.remote.services.wallet

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

const val TEMP_WALLET_BASE_URL = "http://157.230.223.54:9009/"

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
    .baseUrl(TEMP_WALLET_BASE_URL)
    .build()

interface TempWalletApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @GET("wallet/accounts/{user_id}")
    fun getAllUserWalletsAsync(@Path("user_id") userId: Int): Deferred<JsonObject>

    @GET("wallet/set-default-account/{user_id}/{accountNo}")
    fun setDefaultWalletAsync(@Path("user_id") userId: Int,
                              @Path("accountNo") accNo: String): Deferred<JsonObject>

    @POST("wallet/create-wallet")
    fun createNewWalletAsync(@Body params: Map<String, String>): Deferred<JsonObject>
}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object TempWalletApi{
    val retrofitService: TempWalletApiServices by lazy {
        retrofit.create(
            TempWalletApiServices::class.java)
    }
}