package com.wayapaychat.ng.di.modules.local

import com.wayapaychat.local.impl.preference.WayaPreferenceRepositoryImpl
import com.wayapaychat.local.preference.IWayaPreference
import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppLocalModule {

    @Provides
    @Singleton
    fun bindWayaPreferenceRepository(preference: IWayaPreference): IWayaPreferenceRepository =
        WayaPreferenceRepositoryImpl(preference)
}
