package com.wayapaychat.ng.di.modules.remote

import com.wayapaychat.remote.BuildConfig
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppRetrofitModule {

    @Provides
    @Singleton
    @Named(value = WayaAppRemoteConstants.BASE_RETROFIT)
    internal fun provideCleanRetrofit(
        @Named(value = WayaAppRemoteConstants.CLEAN_OKHTTP) okHttpClient: OkHttpClient,
        callAdapterFactory: RxJava2CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        addCallAdapterFactory(callAdapterFactory)
        addConverterFactory(converterFactory)
        baseUrl(BuildConfig.BASE_URL)
    }.build()

    @Provides
    @Singleton
    @Named(value = WayaAppRemoteConstants.PASSWORD_RETROFIT)
    internal fun providePasswordRetrofit(
        @Named(value = WayaAppRemoteConstants.CLEAN_OKHTTP) okHttpClient: OkHttpClient,
        callAdapterFactory: RxJava2CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        addCallAdapterFactory(callAdapterFactory)
        addConverterFactory(converterFactory)
        baseUrl(BuildConfig.BASE_PASSWORD_URL)
    }.build()

    @Provides
    @Singleton
    @Named(value = WayaAppRemoteConstants.PROFILE_RETROFIT)
    internal fun provideProfileRetrofit(
        @Named(value = WayaAppRemoteConstants.CLEAN_OKHTTP) okHttpClient: OkHttpClient,
        callAdapterFactory: RxJava2CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        addCallAdapterFactory(callAdapterFactory)
        addConverterFactory(converterFactory)
        baseUrl(BuildConfig.BASE_PROFILE_URL)
    }.build()

    @Provides
    @Singleton
    @Named(value = WayaAppRemoteConstants.QR_CODE_RETROFIT)
    internal fun provideQRCodeRetrofit(
        @Named(value = WayaAppRemoteConstants.CLEAN_OKHTTP) okHttpClient: OkHttpClient,
        callAdapterFactory: RxJava2CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        addCallAdapterFactory(callAdapterFactory)
        addConverterFactory(converterFactory)
        baseUrl(BuildConfig.BASE_QR_CODE_URL)
    }.build()

    @Provides
    @Singleton
    @Named(value = WayaAppRemoteConstants.WALLET_RETROFIT)
    internal fun provideWalletRetrofit(
        @Named(value = WayaAppRemoteConstants.CLEAN_OKHTTP) okHttpClient: OkHttpClient,
        callAdapterFactory: RxJava2CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        addCallAdapterFactory(callAdapterFactory)
        addConverterFactory(converterFactory)
        baseUrl(BuildConfig.BASE_WALLET_URL)
    }.build()

    @Provides
    @Singleton
    @Named(value = WayaAppRemoteConstants.CARD_RETROFIT)
    internal fun provideCardRetrofit(
        @Named(value = WayaAppRemoteConstants.CLEAN_OKHTTP) okHttpClient: OkHttpClient,
        callAdapterFactory: RxJava2CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        addCallAdapterFactory(callAdapterFactory)
        addConverterFactory(converterFactory)
        baseUrl(BuildConfig.BASE_CARD_URL)
    }.build()

}
