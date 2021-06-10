package com.wayapaychat.core.navigation

import android.content.Context
import android.content.Intent

interface WayaAppIntentResolver <in KEY: WayaAppIntentKey> {

    fun getIntent(context: Context, key: KEY?): Intent
}
