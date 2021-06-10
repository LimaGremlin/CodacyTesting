package com.wayapaychat.remote.services.profile

import com.wayapaychat.remote.base.BaseNetworkModel
import com.wayapaychat.remote.models.profile.GetReferralCodeRemoteResponse
import com.wayapaychat.remote.models.profile.UploadProfilePictureResponse
import com.wayapaychat.remote.models.profile.UserProfileRemoteModel
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileService {

    @PUT("profile-service/update-personal-profile/{user_id}")
    suspend fun updatePersonalProfile(
        @Path("user_id") userId: Int,
        @Body requestBody: HashMap<String, String>
    ): BaseNetworkModel<BaseNetworkModel<UserProfileRemoteModel>>

    @GET("profile-service/profile/{user_id}")
    suspend fun getProfile(
        @Path("user_id") userId: Int
    ): BaseNetworkModel<UserProfileRemoteModel>

    @Multipart
    @PUT("profile-service/update-profile-image/{user_id}")
    suspend fun uploadProfilePic(
        @Path("user_id") userId: Int,
        @Part image: MultipartBody.Part
    ): BaseNetworkModel<UploadProfilePictureResponse>

    @GET("profile-service/referral-code/{user_id}")
    suspend fun getReferralCode(
        @Path("user_id") userId: Int
    ): BaseNetworkModel<GetReferralCodeRemoteResponse>

    @PUT("profile-service/update-corporate-profile/{user_id}")
    suspend fun updateCorporateProfile(
        @Path("user_id") userId: Int,
        @Body requestBody: HashMap<String, String>
    ): BaseNetworkModel<BaseNetworkModel<UserProfileRemoteModel>>

    @GET("profile-service/profile/{userId}")
    suspend fun getUserProfileDetails(@Path("userId") userId: String): BaseNetworkModel<UserProfileRemoteModel>

    @PUT("profile-service/profile/{userId}")
    suspend fun updateUserProfile(
        @Path("userId") userId: String,
        @Body requestBody: HashMap<String, Any>
    ): BaseNetworkModel<UserProfileRemoteModel>

    @GET("password/change/password/{email}")
    suspend fun initiatePasswordChange(@Path("email") email: String)

    @POST("password/reset/password")
    suspend fun changePassword(@Body requestBody: HashMap<String, Any>)

}
