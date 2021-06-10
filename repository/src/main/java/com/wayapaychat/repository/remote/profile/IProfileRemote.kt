package com.wayapaychat.repository.remote.profile

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.auth.UserEntity
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IProfileRemote {

    suspend fun updatePersonalProfile(userId: Int, request: HashMap<String, String>): BaseRepositoryModel<UserEntity>

    suspend fun getProfile(userId: Int): BaseRepositoryModel<UserEntity>

    suspend fun updateProfilePicture(userId: Int, profilePictureFile: File): BaseRepositoryModel<String>

    suspend fun getReferralCode(userId: Int): BaseRepositoryModel<String>

    suspend fun updateCorporateProfile(userId: Int, request: HashMap<String, String>): BaseRepositoryModel<UserEntity>

    suspend fun updateUserInformation(userId: String, request: HashMap<String, Any>): BaseRepositoryModel<Boolean>

    suspend fun initiateChangePassword(email: String): BaseRepositoryModel<Nothing?>

    suspend fun changePassword(request: HashMap<String, Any>): BaseRepositoryModel<Nothing?>

}
