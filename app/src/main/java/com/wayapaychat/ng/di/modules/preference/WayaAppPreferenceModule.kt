package com.wayapaychat.ng.di.modules.preference

import android.content.Context
import com.wayapaychat.local.preference.IWayaPreference
import com.wayapaychat.preference.impl.WayaPreferenceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppPreferenceModule {

    @Provides
    @Singleton
    fun bindWayaAppPreference(
        @ApplicationContext context: Context
    ): IWayaPreference = WayaPreferenceImpl(context)
}
