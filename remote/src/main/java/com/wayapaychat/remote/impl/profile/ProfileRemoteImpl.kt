package com.wayapaychat.remote.impl.profile

import com.wayapaychat.remote.mappers.auth.toUserEntity
import com.wayapaychat.remote.mappers.profile.toUserEntity
import com.wayapaychat.remote.models.auth.UserRemoteeModel
import com.wayapaychat.remote.services.profile.ProfileService
import com.wayapaychat.remote.utils.WayaAppExceptionHandler
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.auth.UserEntity
import com.wayapaychat.repository.remote.profile.IProfileRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class ProfileRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.PROFILE_RETROFIT) retrofit: Retrofit,
    @Named(value = WayaAppRemoteConstants.PASSWORD_RETROFIT) passwordRetrofit: Retrofit
) : IProfileRemote {

    private val profileService = retrofit.create(ProfileService::class.java)
    private val passwordRetrofit = passwordRetrofit.create(ProfileService::class.java)

    override suspend fun updatePersonalProfile(
        userId: Int,
        request: HashMap<String, String>
    ): BaseRepositoryModel<UserEntity> {
        return try {
            val response = profileService.updatePersonalProfile(userId, request)
            BaseRepositoryModel(
                successful = response.success,
                message = response.message,
                data = response.data?.data?.toUserEntity(userId)
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                successful = false,
                message = WayaAppExceptionHandler.getErrorFromThrowable(throwable),
                data = UserRemoteeModel().toUserEntity()
            )
        }
    }

    override suspend fun getProfile(userId: Int): BaseRepositoryModel<UserEntity> {
        return try {
            val response = profileService.getProfile(userId)
            BaseRepositoryModel(
                successful = response.success,
                message = response.message,
                data = response.data?.toUserEntity(userId)
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                successful = false,
                message = WayaAppExceptionHandler.getErrorFromThrowable(throwable),
                data = UserRemoteeModel().toUserEntity()
            )
        }
    }

    override suspend fun updateProfilePicture(
        userId: Int,
        profilePictureFile: File
    ): BaseRepositoryModel<String> {
        return try {
            val reqFile: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), profilePictureFile)
            val requestBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", profilePictureFile.name, reqFile)
            val response =
                profileService.uploadProfilePic(userId, requestBody)
                BaseRepositoryModel(
                    response.success,
                    response.data?.imageUrl,
                    response.message
                )
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                BaseRepositoryModel(
                    false,
                    "",
                    WayaAppExceptionHandler.getErrorFromThrowable(
                        throwable
                    )
                )
            }
        }

    override suspend fun getReferralCode(userId: Int): BaseRepositoryModel<String> {
        try {
            val response =
                profileService.getReferralCode(userId)
            return BaseRepositoryModel(
                response.success,
                response.data?.referralCodeDataRemoteModel?.referralCode,
                response.message
            )
        } catch (throwable: Throwable) {
            return BaseRepositoryModel(
                false,
                "",
                WayaAppExceptionHandler.getErrorFromThrowable(
                    throwable
                )
            )
        }
    }

    override suspend fun updateCorporateProfile(
        userId: Int,
        request: HashMap<String, String>
    ): BaseRepositoryModel<UserEntity>  {
        return try {
            val response = profileService.updateCorporateProfile(userId, request)
            BaseRepositoryModel(
                successful = response.success,
                message = response.message,
                data = response.data?.data?.toUserEntity(userId)
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                successful = false,
                message = WayaAppExceptionHandler.getErrorFromThrowable(throwable),
                data = UserRemoteeModel().toUserEntity()
            )
        }
    }

    override suspend fun updateUserInformation(
        userId: String,
        request: HashMap<String, Any>
    ): BaseRepositoryModel<Boolean> {
        return try {
            val response = profileService.getUserProfileDetails(userId)
            BaseRepositoryModel(
                successful = response.success,
                message = response.data?.message,
                data = response.data?.status
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                successful = false,
                message = WayaAppExceptionHandler.getErrorFromThrowable(throwable),
                data = false
            )
        }
    }

    override suspend fun initiateChangePassword(email: String): BaseRepositoryModel<Nothing?> {
        return try {
            passwordRetrofit.initiatePasswordChange(email)
            BaseRepositoryModel<Nothing?>(
                successful = true,
                message = "Successful",
                data = null
            )
        }catch(throwable: Throwable) {
            BaseRepositoryModel<Nothing?>(
                successful = false,
                message = WayaAppExceptionHandler.getErrorFromThrowable(throwable),
                data = null
            )
        }
    }

    override suspend fun changePassword(request: HashMap<String, Any>): BaseRepositoryModel<Nothing?> {
        return try {
            passwordRetrofit.changePassword(request)
            BaseRepositoryModel<Nothing?>(
                successful = true,
                message = "Successful",
                data = null
            )
        }catch(throwable: Throwable) {
            BaseRepositoryModel<Nothing?>(
                successful = false,
                message = WayaAppExceptionHandler.getErrorFromThrowable(throwable),
                data = null
            )
        }
    }

}
