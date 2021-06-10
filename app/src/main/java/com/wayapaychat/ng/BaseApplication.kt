package com.wayapaychat.ng

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.wayapaychat.core.utils.BaseAppLog
import dagger.hilt.android.HiltAndroidApp
import io.fabric.sdk.android.Fabric

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseAppLog.init()
        Stetho.initializeWithDefaults(this)
        //Fabric.with(this, Crashlytics())
//        Fresco.initialize(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
