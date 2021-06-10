package com.wayapaychat.preference.impl

import android.content.Context
import androidx.core.content.edit
import com.wayapaychat.local.preference.IWayaPreference
import com.wayapaychat.preference.utils.WayaPreferenceConstants
import javax.inject.Inject

/**
 * Created by ayokunlepaul on 2019-07-17
 */
class WayaPreferenceImpl @Inject constructor(
    context: Context
) : IWayaPreference {

    private val WayaAppPreference =
        context.getSharedPreferences(WayaPreferenceConstants.RIDER_PREFERENCE, Context.MODE_PRIVATE)

    override fun saveToken(type: Int, token: String) {
        if (type == WayaPreferenceConstants.TYPE_PERSONAL) WayaAppPreference.edit {
            putString(WayaPreferenceConstants.RIDER_TOKEN, token)
        } else {
            WayaAppPreference.edit {
                putString(WayaPreferenceConstants.PILOT_TOKEN, token)
            }
        }
    }

    override fun saveRegistrationStatus(fullyRegistered: Boolean) {
        WayaAppPreference.edit {
            putBoolean(WayaPreferenceConstants.IS_USER_FULLY_REGISTERED, fullyRegistered)
            apply()
        }
    }

    override fun isUserFullyRegistered(): Boolean =
        WayaAppPreference.getBoolean(WayaPreferenceConstants.IS_USER_FULLY_REGISTERED, false)

    override fun getToken(type: Int): String? =
        if (type == WayaPreferenceConstants.TYPE_PERSONAL) WayaAppPreference.getString(
            WayaPreferenceConstants.RIDER_TOKEN,
            null
        )
        else WayaAppPreference.getString(WayaPreferenceConstants.PILOT_TOKEN, null)

    override fun saveLoggedInUserType(type: Int) {
        WayaAppPreference.edit {
            putInt(WayaPreferenceConstants.LOGGED_IN_USER, type)
            apply()
        }
    }

    override fun getLoggedInUserType(): Int =
        WayaAppPreference.getInt(WayaPreferenceConstants.LOGGED_IN_USER, 0)

    override fun setLoggedIn(type: Int) {
        if (type == WayaPreferenceConstants.TYPE_PERSONAL) WayaAppPreference.edit {
            putBoolean(WayaPreferenceConstants.LOGIN_TOKEN, true)
        } else {
            WayaAppPreference.edit {
                putBoolean(WayaPreferenceConstants.LOGIN_TOKEN, true)
            }
        }
    }

    override fun isLoggedIn(type: Int): Boolean =
        if (type == WayaPreferenceConstants.TYPE_PERSONAL) WayaAppPreference.getBoolean(
            WayaPreferenceConstants.LOGIN_TOKEN,
            false
        )
        else WayaAppPreference.getBoolean(WayaPreferenceConstants.LOGIN_TOKEN, false)

    override fun logOut(type: Int) =
        if (type == WayaPreferenceConstants.TYPE_PERSONAL) WayaAppPreference.edit { clear() }
        else WayaAppPreference.edit { clear() }

    override fun saveValidationToken(token: String) {
        WayaAppPreference.edit {
            putString(WayaPreferenceConstants.VALIDATION_TOKEN, token)
            apply()
        }
    }

    override fun getValidationToken(): String? =
        WayaAppPreference.getString(WayaPreferenceConstants.VALIDATION_TOKEN, null)

    override fun hasCompletedWalletSetup(): Boolean =
        WayaAppPreference.getBoolean(WayaPreferenceConstants.HAS_COMPLETED_WALLET_SETUP, false)

    override fun setWalletSetupCompletionStatus(hasCompletedWalletSetup: Boolean) {
        WayaAppPreference.edit {
            putBoolean(WayaPreferenceConstants.HAS_COMPLETED_WALLET_SETUP, hasCompletedWalletSetup)
            apply()
        }
    }

    override fun clearUserPreference() = WayaAppPreference.edit { clear() }

    override fun getHasFetchedUserProfile(): Boolean =
        WayaAppPreference.getBoolean(WayaPreferenceConstants.HAS_FETCHED_USER_PROFILE, false)

    override fun hasFetchedUserProfile() {
        WayaAppPreference.edit {
            putBoolean(WayaPreferenceConstants.HAS_FETCHED_USER_PROFILE, true)
            apply()
        }
    }

    override fun getReferralCode(): String =
        WayaAppPreference.getString(WayaPreferenceConstants.REFERRAL_CODE, "") ?: ""

    override fun setReferralCode(referralCode: String) {
        WayaAppPreference.edit {
            putString(WayaPreferenceConstants.REFERRAL_CODE, referralCode)
            apply()
        }
    }

    override fun getUserLandingSelection(): Int =
        WayaAppPreference.getInt(WayaPreferenceConstants.USER_LANDING, -1)


    override fun setUserLandingSelection(landingConstant: Int) {
        WayaAppPreference.edit {
            putInt(WayaPreferenceConstants.USER_LANDING, landingConstant)
            apply()
        }
    }

    override fun saveLoginHistoryId(id: Int) {
        WayaAppPreference.edit {
            putInt(WayaPreferenceConstants.LOGIN_HISTORY_ID, id)
            apply()
        }
    }

    override fun getLoginHistoryId(): Int =
        WayaAppPreference.getInt(WayaPreferenceConstants.LOGIN_HISTORY_ID, 0)

}
