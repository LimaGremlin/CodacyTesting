package com.wayapaychat.navigation.utils

import android.content.Context
import android.content.Intent
import com.wayapaychat.navigation.R

object AppActivityNavCommands {

    fun getAuthIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.authHome))

    fun getPaymentIntent(context: Context, navigateTo: String = ""): Intent =
        navigationIntent(context, context.getString(R.string.paymentHome)).apply {
            putExtra(context.getString(R.string.paymentNavToExtra), navigateTo)
        }

    fun getSettingsIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.settingsHome))

    fun getProfileIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.profileHome))

    fun getOnBoardingIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.onBoardingHome))

    fun getWalletIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.walletHome))

    fun getLandingIntent(context: Context): Intent {
        //Close all activities in back-stack
        val intent = navigationIntent(context, context.getString(R.string.onLandingHome))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    fun getQrCodeProfileIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.qrCodeProfileHome))

    fun getSearchIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.searchHome))

    fun getGramProfileIntent(context: Context): Intent =
        navigationIntent(context, context.getString(R.string.gramProfileHome))

    private fun navigationIntent(context: Context, navAction: String) =
        Intent(navAction).setPackage(context.packageName)
}
