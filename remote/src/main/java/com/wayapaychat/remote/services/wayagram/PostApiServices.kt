package com.wayapaychat.remote.services.wayagram

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wayapaychat.remote.services.models.PostAsysnc
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val POST_BASE_URL = "http://157.245.84.14:3200/"

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
    .baseUrl(POST_BASE_URL)
    .build()

interface PostApiServices {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @Multipart
    @POST("create")
    fun createPostAsync(@PartMap partMap: MutableMap<String, RequestBody>,
                        @Part img: MultipartBody.Part): Deferred<JsonObject>
    @POST("create")
    fun createPostAsync(@Body param: PostAsysnc): Deferred<JsonObject>

    @Multipart
    @PUT("update")
    fun updatePostAsync(@PartMap partMap: HashMap<String, RequestBody>,
                        @Part img: MultipartBody.Part): Deferred<JsonObject>

    @PUT("create")
    fun updatePostAsync(@Body param: HashMap<String, String>): Deferred<JsonObject>

    @GET("search")
    fun queryPostAsync(@Query("query") query: String): Deferred<JsonObject>

    @GET("user-posts")
    fun queryUserPostAsync(@Query("profile_id") gramId: String): Deferred<JsonObject>

    @POST("vote")
    fun voteAPostAsync(@Body param: HashMap<String, String>) : Deferred<JsonObject>

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PostApi{
    val retrofitService: PostApiServices by lazy {
        retrofit.create(
            PostApiServices::class.java)
    }
}