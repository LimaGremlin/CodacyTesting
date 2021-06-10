package com.wayapaychat.repository.impl.profile

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import com.wayapaychat.repository.mappers.auth.toDomainModel
import com.wayapaychat.repository.remote.profile.IProfileRemote
import kotlinx.coroutines.flow.Flow
import java.io.File

class ProfileRepositoryImpl(
    private val profileRemote: IProfileRemote,
    private val iAuthenticationLocal: IAuthenticationLocal,
    private val wayaPreferenceRepository: IWayaPreferenceRepository
) : IProfileRepository {

    override suspend fun updatePersonalProfile(request: HashMap<String, String>): BaseDomainModel<Nothing?> {
        val userData = iAuthenticationLocal.getSavedUserData()
        val userId = userData.user.id

        val response = profileRemote.updatePersonalProfile(userData.user.id, request)
        userData.user = response.data!!
        userData.user.id = userId
        iAuthenticationLocal.saveUserDetails(userData)
        return BaseDomainModel(
            response.successful,
            null,
            response.message
        )
    }

    override suspend fun getProfileDetails(): BaseDomainModel<UserDomainModel> {
        val userData = iAuthenticationLocal.getSavedUserData()
        return if (wayaPreferenceRepository.getHasFetchedUserProfile()) {
            BaseDomainModel(
                true,
                userData.user.toDomainModel(userData.corporate),
                "Successfully fetched Profile"
            )
        } else {
            val userId = userData.user.id
            val response = profileRemote.getProfile(userId)
            userData.user = response.data!!
            userData.user.id = userId
            iAuthenticationLocal.saveUserDetails(userData)
            wayaPreferenceRepository.hasFetchedUserProfile()
            BaseDomainModel(
                response.successful,
                response.data.toDomainModel(userData.corporate),
                response.message
            )
        }
    }

    override suspend fun updateProfilePicture(profilePictureFile: File): BaseDomainModel<String> {
        val userData = iAuthenticationLocal.getSavedUserData()
        val response = profileRemote.updateProfilePicture(userData.user.id, profilePictureFile)
        response.data?.let {
            userData.user.profileImage = it
            iAuthenticationLocal.saveUserDetails(userData)
        }
        return BaseDomainModel(
            response.successful,
            response.data,
            response.message
        )
    }

    override suspend fun getReferralCode(): BaseDomainModel<String> {
        val userData = iAuthenticationLocal.getSavedUserData()

        val prefReferralCode = wayaPreferenceRepository.getReferralCode()
        return if (prefReferralCode.isEmpty()) {
            val response = profileRemote.getReferralCode(userData.user.id)
            response.data?.let {
                wayaPreferenceRepository.setReferralCode(it)
            }
            BaseDomainModel(
                response.successful,
                response.data,
                response.message
            )
        } else {
            BaseDomainModel(
                true,
                prefReferralCode,
                ""
            )
        }
    }

    override suspend fun updateUserInformation(
        userId: String,
        request: HashMap<String, Any>
    ): Flow<BaseDomainModel<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCorporateProfile(request: HashMap<String, String>): BaseDomainModel<Nothing?> {
        val userData = iAuthenticationLocal.getSavedUserData()
        val userId = userData.user.id

        val response = profileRemote.updateCorporateProfile(userId, request)
        userData.user = response.data!!
        userData.user.id = userId
        iAuthenticationLocal.saveUserDetails(userData)
        return BaseDomainModel(
            response.successful,
            null,
            response.message
        )
    }

    override suspend fun initiateChangePassword(): BaseDomainModel<Nothing?> {
        val userData = iAuthenticationLocal.getSavedUserData()
        val email = userData.user.email

        val response = profileRemote.initiateChangePassword(email)
        return BaseDomainModel<Nothing?>(
            response.successful,
            null,
            response.message
        )
    }

    override suspend fun changePassword(request: HashMap<String, Any>): BaseDomainModel<Nothing?> {
        val userData = iAuthenticationLocal.getSavedUserData()
        val email = userData.user.email
        request["email"] = email

        val response = profileRemote.changePassword(request)
        return BaseDomainModel<Nothing?>(
            response.successful,
            null,
            response.message
        )
    }

}
