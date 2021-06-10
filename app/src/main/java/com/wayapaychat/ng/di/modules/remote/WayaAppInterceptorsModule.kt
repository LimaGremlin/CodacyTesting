package com.wayapaychat.ng.di.modules.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.wayapaychat.core.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppInterceptorsModule {

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor (
        //ToDo 1.1 Find out why it is not causing error from Ebenezer
//        HttpLoggingInterceptor.Logger { log ->
//            Timber.e(log)
//        }
    ).apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    internal fun provideStethoLoggingInterceptor(): StethoInterceptor = StethoInterceptor()
}
