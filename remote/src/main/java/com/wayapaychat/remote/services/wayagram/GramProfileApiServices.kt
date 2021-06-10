package com.wayapaychat.remote.services.wayagram

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val WAYA_GRAM_BASE_URL = "http://157.245.84.14:2200/"

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
    .baseUrl(WAYA_GRAM_BASE_URL)
    .build()

interface GramProfileApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @GET("get-by-user-id")
    fun getProfileByUIAsync(@Query("user_id") uId: Int): Deferred<JsonObject>

    @GET("search")
    fun queryUserAsync(@Query("query") query: String, @Query("profile_id") profileID: String):Deferred<JsonObject>

    @PUT("update")
    fun updateProfileAsync(@Body param: HashMap<String, String>): Deferred<JsonObject>

    @Multipart
    @PUT("update")
    fun updateProfileAsync(@PartMap partMap: HashMap<String, RequestBody>,
                         @Part img: MultipartBody.Part): Deferred<JsonObject>

    @Multipart
    @PUT("update")
    fun updateProfileAsync(@PartMap partMap: HashMap<String, RequestBody>,
                         @Part profileImage: MultipartBody.Part,
                         @Part coverImage: MultipartBody.Part): Deferred<JsonObject>

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object GramProfileApi{
    val RETROFIT_SERVICE: GramProfileApiServices by lazy {
        retrofit.create(
            GramProfileApiServices::class.java)
    }
}