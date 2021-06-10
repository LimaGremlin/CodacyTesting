package com.wayapaychat.ng.di.modules

import com.wayapaychat.core.utils.WayaRxExecutionThread
import com.wayapaychat.domain.utils.RxExecutionThread
import com.wayapaychat.ng.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppCoreUtilityModule {

    @Provides
    @Singleton
    fun bindRxExecutionThread(): RxExecutionThread = WayaRxExecutionThread()

    @Provides
    @Singleton
    fun providesBaseApplication(): BaseApplication = BaseApplication()
}
