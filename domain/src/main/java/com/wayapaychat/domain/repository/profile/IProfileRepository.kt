package com.wayapaychat.domain.repository.profile

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IProfileRepository {

    suspend fun updatePersonalProfile(request: HashMap<String, String>): BaseDomainModel<Nothing?>

    suspend fun updateCorporateProfile(request: HashMap<String, String>): BaseDomainModel<Nothing?>

    suspend fun getProfileDetails(): BaseDomainModel<UserDomainModel>

    suspend fun updateProfilePicture(profilePictureFile: File): BaseDomainModel<String>

    suspend fun getReferralCode(): BaseDomainModel<String>

    suspend fun updateUserInformation(
        userId: String,
        request: HashMap<String, Any>
    ): Flow<BaseDomainModel<Boolean>>

    suspend fun initiateChangePassword(): BaseDomainModel<Nothing?>

    suspend fun changePassword(request: HashMap<String, Any>): BaseDomainModel<Nothing?>

}
