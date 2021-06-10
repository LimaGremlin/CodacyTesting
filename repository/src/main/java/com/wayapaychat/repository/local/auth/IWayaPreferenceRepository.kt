package com.wayapaychat.repository.local.auth

interface IWayaPreferenceRepository {

    fun saveToken(type: Int, token: String)

    fun getToken(type: Int): String?

    fun saveLoggedInUserType(type: Int)

    fun getLoggedInUserType(): Int

    fun setLoggedIn(type: Int)

    fun isLoggedIn(type: Int): Boolean

    fun logOut(type: Int)

    fun isUserFullyRegistered(): Boolean

    fun saveFullyRegisteredStatus(fullyRegistered: Boolean = false)

    fun clearPreference()

    fun getHasFetchedUserProfile(): Boolean

    fun hasFetchedUserProfile()

    fun getReferralCode(): String

    fun setReferralCode(referralCode: String)

    fun getUserLandingSelection(): Int

    fun setUserLandingSelection(landingConstant: Int)

    fun saveLoginHistoryId(id: Int)

    fun getLoginHistoryId(): Int

}
