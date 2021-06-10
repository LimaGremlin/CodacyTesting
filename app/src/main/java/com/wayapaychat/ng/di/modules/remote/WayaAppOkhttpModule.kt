package com.wayapaychat.ng.di.modules.remote

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.wayapaychat.remote.interceptor.WayaAppAuthorizationTokenInterceptor
import com.wayapaychat.remote.interceptor.responseinterceptor.AuthenticationResponseInterceptor
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppOkhttpModule {

    @Provides
    @Singleton
    internal fun provideFile(@ApplicationContext context: Context): File = File(
        context.cacheDir, WayaAppRemoteConstants.OKHTTP_CACHE
    )

    @Provides
    @Singleton
    internal fun provideCache(file: File): Cache = Cache(
        file, WayaAppRemoteConstants.OKHTTP_CACHE_SIZE.toLong()
    )

    @Provides
    @Singleton
    @Named(value = WayaAppRemoteConstants.CLEAN_OKHTTP)
    internal fun provideCleanOkhttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authenticationResponseInterceptor: AuthenticationResponseInterceptor,
        authorizationTokenInterceptor: WayaAppAuthorizationTokenInterceptor,
        stethoInterceptor: StethoInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(authenticationResponseInterceptor)
            addInterceptor(authorizationTokenInterceptor)
            addNetworkInterceptor(stethoInterceptor)
            cache(cache)
            followRedirects(true)
            followSslRedirects(true)
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
        }.build()
    }
}
