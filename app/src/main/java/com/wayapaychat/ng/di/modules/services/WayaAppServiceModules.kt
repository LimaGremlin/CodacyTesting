package com.wayapaychat.ng.di.modules.services

import com.wayapaychat.ng.services.WayaAppFirebaseMessagingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WayaAppServiceModules {

    @Provides
    @ServiceScoped
    fun bindGokadaRideHailingService(): WayaAppFirebaseMessagingService  = WayaAppFirebaseMessagingService()
}
