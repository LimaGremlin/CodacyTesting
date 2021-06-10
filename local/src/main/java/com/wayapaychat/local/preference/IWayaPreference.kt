package com.wayapaychat.local.preference

interface IWayaPreference {

    fun saveToken(type: Int, token: String)

    fun getToken(type: Int): String?

    fun saveLoggedInUserType(type: Int)

    fun getLoggedInUserType(): Int

    fun setLoggedIn(type: Int)

    fun isLoggedIn(type: Int): Boolean

    fun logOut(type: Int)

    fun saveValidationToken(token: String)

    fun getValidationToken(): String?

    fun isUserFullyRegistered(): Boolean

    fun saveRegistrationStatus(fullyRegistered: Boolean)

    fun hasCompletedWalletSetup(): Boolean

    fun setWalletSetupCompletionStatus(hasCompletedWalletSetup: Boolean)

    fun clearUserPreference()

    fun getHasFetchedUserProfile(): Boolean

    fun hasFetchedUserProfile()

    fun getReferralCode(): String

    fun setReferralCode(referralCode: String)

    fun getUserLandingSelection(): Int

    fun setUserLandingSelection(landingConstant: Int)

    fun saveLoginHistoryId(id: Int)

    fun getLoginHistoryId(): Int

}
