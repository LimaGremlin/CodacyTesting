package com.wayapaychat.local.impl.preference

import com.wayapaychat.local.preference.IWayaPreference
import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import javax.inject.Inject

class WayaPreferenceRepositoryImpl @Inject constructor(
    private val preference: IWayaPreference
) : IWayaPreferenceRepository {

    override fun isUserFullyRegistered(): Boolean = preference.isUserFullyRegistered()

    override fun saveFullyRegisteredStatus(fullyRegistered: Boolean) =
        preference.saveRegistrationStatus(fullyRegistered)

    override fun saveToken(type: Int, token: String) = preference.saveToken(
        type = type,
        token = token
    )

    override fun getToken(type: Int): String? = preference.getToken(type)

    override fun saveLoggedInUserType(type: Int) = preference.saveLoggedInUserType(type)

    override fun getLoggedInUserType(): Int = preference.getLoggedInUserType()

    override fun setLoggedIn(type: Int) = preference.setLoggedIn(type)

    override fun isLoggedIn(type: Int): Boolean = preference.isLoggedIn(type)

    override fun logOut(type: Int) = preference.logOut(type)

    override fun clearPreference() = preference.clearUserPreference()

    override fun getHasFetchedUserProfile(): Boolean =
        preference.getHasFetchedUserProfile()

    override fun hasFetchedUserProfile() =
        preference.hasFetchedUserProfile()

    override fun getReferralCode(): String =
        preference.getReferralCode()

    override fun setReferralCode(referralCode: String) {
        preference.setReferralCode(referralCode)
    }

    override fun getUserLandingSelection(): Int = preference.getUserLandingSelection()

    override fun setUserLandingSelection(landingConstant: Int) {
        preference.setUserLandingSelection(landingConstant)
    }

    override fun saveLoginHistoryId(id: Int) {
        preference.saveLoginHistoryId(id)
    }

    override fun getLoginHistoryId(): Int =
        preference.getLoginHistoryId()

}
