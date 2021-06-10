package com.wayapaychat.core.navigation

import android.content.Context
import android.content.Intent
import javax.inject.Inject

class  WayaAppNavigator @Inject constructor(
    private val wayaAppIntentResolvers: WayaAppIntentResolverMap
) {

    fun createIntent (context: Context, intentKey: WayaAppIntentKey): Intent {
        val resolver = wayaAppIntentResolvers[intentKey::class.java]?.get() as WayaAppIntentResolver<WayaAppIntentKey>?
        return resolver?.getIntent(context, intentKey) ?: throw IllegalArgumentException("Cannot resolve intent key $intentKey")
    }
}
