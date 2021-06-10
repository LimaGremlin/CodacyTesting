package com.wayapaychat.remote.services.wayagram

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wayapaychat.remote.services.models.EVentAsync
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val EVENT_BASE_URL = "http://157.245.84.14:6003/"

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
    .baseUrl(EVENT_BASE_URL)
    .build()

interface EVentApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @POST("create-event")
    fun createEventAsync(@Body param: EVentAsync): Deferred<JsonObject>

    @Multipart
    @POST("create-event")
    fun createEventAsync(@PartMap partMap: HashMap<String, RequestBody>,
                         @Part img: MultipartBody.Part): Deferred<JsonObject>

    @PUT("update-event")
    fun updateEVentAsync(@Body param: HashMap<String, String>): Deferred<JsonObject>

    @Multipart
    @PUT("update-event")
    fun updateEVentAsync(@PartMap partMap: HashMap<String, RequestBody>,
                         @Part img: MultipartBody.Part): Deferred<JsonObject>

    @GET("get-all-user-events")
    fun getUserEventAsync(@Query("userId") uid: String, @Query("pageNumber") pNumber: Int): Deferred<JsonObject>

    @GET("get-all-events")
    fun getAllEventsAsync(@Query("pageNumber") pNo: Int = 1): Deferred<JsonObject>

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object EventApi{
    val retrofitService: EVentApiServices by lazy {
        retrofit.create(
            EVentApiServices::class.java)
    }
}