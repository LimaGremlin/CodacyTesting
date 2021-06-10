package com.wayapaychat.ng.di.modules.remote

import com.wayapaychat.local.impl.auth.WayaAuthenticationLocalImpl
import com.wayapaychat.local.preference.IWayaPreference
import com.wayapaychat.local.room.dao.OnBoardingStatusDao
import com.wayapaychat.local.room.dao.UserDataDao
import com.wayapaychat.remote.impl.auth.AuthRemoteImpl
import com.wayapaychat.remote.impl.auth.LoginHistoryRemoteImpl
import com.wayapaychat.remote.impl.payment.PaymentRemoteImpl
import com.wayapaychat.remote.impl.profile.ProfileRemoteImpl
import com.wayapaychat.remote.impl.samples.CancelReasonRemoteImpl
import com.wayapaychat.remote.impl.samples.ManualRideRemoteImpl
import com.wayapaychat.remote.impl.wallet.WalletRemoteImpl
import com.wayapaychat.remote.mappers.samples.CancelReasonsLocalModelMapper
import com.wayapaychat.remote.mappers.samples.FareCalculatorRemoteModelMapper
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.remote.auth.IAuthRemote
import com.wayapaychat.repository.remote.auth.ILoginHistoryRemote
import com.wayapaychat.repository.remote.payment.IPaymentRemote
import com.wayapaychat.repository.remote.profile.IProfileRemote
import com.wayapaychat.repository.remote.sample.CancelReasonsRemote
import com.wayapaychat.repository.remote.sample.ManualRideRemote
import com.wayapaychat.repository.remote.wallet.IWalletRemote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppRemoteModule {

    @Provides
    @Singleton
    fun bindAuthLocalRemote(
        onBoardingStatusDao: OnBoardingStatusDao,
        preference: IWayaPreference,
        userDataDao: UserDataDao
    ): IAuthenticationLocal =
        WayaAuthenticationLocalImpl(onBoardingStatusDao, userDataDao, preference)

    @Provides
    @Singleton
    fun bindDriverRemote(
        @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit,
        fareCalculatorRemoteModelMapper: FareCalculatorRemoteModelMapper
    ): ManualRideRemote = ManualRideRemoteImpl(retrofit, fareCalculatorRemoteModelMapper)

    @Provides
    @Singleton
    fun bindCancelReasonsRemote(
        @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit,
        cancelReasonsLocalModelMapper: CancelReasonsLocalModelMapper
    ): CancelReasonsRemote = CancelReasonRemoteImpl(retrofit, cancelReasonsLocalModelMapper)

    @Provides
    @Singleton
    fun bindRiderRemote(
        @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit,
        @Named(value = WayaAppRemoteConstants.PASSWORD_RETROFIT) passwordRetrofit: Retrofit,
    ): IAuthRemote = AuthRemoteImpl(retrofit, passwordRetrofit)

    @Provides
    @Singleton
    fun bindProfileRemote(
        @Named(value = WayaAppRemoteConstants.PROFILE_RETROFIT) profileRetrofit: Retrofit,
        @Named(value = WayaAppRemoteConstants.PASSWORD_RETROFIT) passwordRetrofit: Retrofit
    ): IProfileRemote = ProfileRemoteImpl(profileRetrofit, passwordRetrofit)

    @Provides
    @Singleton
    fun bindPaymentRemote(
        @Named(value = WayaAppRemoteConstants.QR_CODE_RETROFIT) qrCodeRetrofit: Retrofit,
        @Named(value = WayaAppRemoteConstants.CARD_RETROFIT) cardRetrofit: Retrofit,
    ): IPaymentRemote = PaymentRemoteImpl(qrCodeRetrofit, cardRetrofit)

    @Provides
    @Singleton
    fun bindLoginHistoryRemote(
        @Named(value = WayaAppRemoteConstants.BASE_RETROFIT) retrofit: Retrofit
    ): ILoginHistoryRemote = LoginHistoryRemoteImpl(retrofit)

    @Provides
    @Singleton
    fun bindWalletRemote(
        @Named(value = WayaAppRemoteConstants.WALLET_RETROFIT) retrofit: Retrofit
    ): IWalletRemote = WalletRemoteImpl(retrofit)

//    @Provides
//    @Singleton
//    fun bindAuthProfileRemote(
//        @Named(value = WayaAppRemoteConstants.PROFILE_RETROFIT) profileRetrofit: Retrofit
//    ): ProfileRemote = IProfileRepositoryImpl(profileRetrofit)
}
