package com.wayapaychat.remote.services.card

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wayapaychat.remote.services.wallet.WalletApiServices
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

//http://157.230.223.54:3020/card-service/api/card/list

const val CARD_BASE_URL = "http://157.230.223.54:3020/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



private var clientBuilder = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(clientBuilder.build())
    .baseUrl(CARD_BASE_URL)
    .build()

interface CardAPIService {

    @GET("card-service/api/card/list")
    fun getAllCardsAsync (@Header("Authorization") authHeader: String): Deferred<JsonObject>


    //
    @POST("card-service/api/card/add")
    fun addCardsAsync (@Header("Authorization") authHeader: String,
                       @Body params: Map<String, String>): Deferred<JsonObject>


}
object CardApi{
    val retrofitService: CardAPIService by lazy {
        retrofit.create(
            CardAPIService::class.java)
    }
}