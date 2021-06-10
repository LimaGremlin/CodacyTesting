package com.wayapaychat.core.newtwork

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val LOG_IN_HISTORY_BASE_URL = "http://68.183.60.114:8059/"

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
    .baseUrl(LOG_IN_HISTORY_BASE_URL)
    .build()

interface LogInHistoryApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @POST("history/save")
    fun saveLogInHistoryAsync(@Body params: Map<String, String>,
                             @Header("authorization") authHeader: String): Deferred<Response<JsonObject>>

    @GET("history/user-history/{userId}")
    fun getUserLoginHistoryAsync(@Path("userId") userId: Int): Deferred<JsonObject>

    @GET("history/user-last-login/{userId}")
    fun getLastKnownHistoryAsync(@Path("userId") userId: Int): Deferred<JsonObject>

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object LogInHistoryApi{
    val retrofitService: LogInHistoryApiServices by lazy {
        retrofit.create(LogInHistoryApiServices::class.java)
    }
}