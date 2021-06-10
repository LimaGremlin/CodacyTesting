package com.wayapaychat.remote.services.wayagram

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
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

const val PAGES_BASE_URL = "http://157.245.84.14:6002/"

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
    .baseUrl(PAGES_BASE_URL)
    .build()

interface PagesApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @POST("create-page")
    fun createPageAsync(@Body param: PagesAsync): Deferred<JsonObject>

    @Multipart
    @POST("create-page")
    fun createPageAsync(@PartMap partMap: HashMap<String, RequestBody>,
                         @Part img: MultipartBody.Part): Deferred<JsonObject>

    @Multipart
    @POST("create-page")
    fun createPageAsync(@PartMap partMap: HashMap<String, RequestBody>,
                        @Part profileImage: MultipartBody.Part,
                        @Part coverImage: MultipartBody.Part): Deferred<JsonObject>

    @GET("get-all-page-categories")
    fun getAllPagesCategoryAsync(@Query("pageNumber") pNumber: Int): Deferred<JsonObject>

    @GET("get-all-user-pages")
    fun getUserPagesAsync(@Query("userId") uid: String): Deferred<JsonObject>

    @GET("search-pages")
    fun queryPagesAsync(@Query("query") query: String,
                        @Query("pageNumber") number: Int,
                        @Query("userId") userId: String):Deferred<JsonObject>

    @PUT("follow-or-un-follow")
    fun followUnFollowPageAsync(@Body param: Map<String, String>): Deferred<JsonObject>

    @GET("get-all-page-followers")
    fun getAllPageFollowersAsync(@Query("userId") userId:String,
                                 @Query("pageId") pageId: String): Deferred<JsonObject>

    @GET("get-all-pages-a-user-follows")
    fun getFollowingPagesAsync(@Query("userId") userId: String,
                               @Query("pageNumber") pageNumber: Int): Deferred<JsonObject>

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PagesApi{
    val retrofitService: PagesApiServices by lazy {
        retrofit.create(PagesApiServices::class.java)
    }
}