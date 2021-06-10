package com.wayapaychat.ng.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by edetebenezer on 2019-09-17
 **/
@Module
@InstallIn(SingletonComponent::class)
class WayaAppExecutorModule {

    @Provides
    fun getExecutor(): Executor {
        return Executors.newFixedThreadPool(2)
    }
}
