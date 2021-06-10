package com.wayapaychat.ng.di.modules.repository

import com.wayapaychat.domain.repository.CancelReasonsRepository
import com.wayapaychat.domain.repository.IDriverDetailsRepository
import com.wayapaychat.domain.repository.auth.AuthRepository
import com.wayapaychat.domain.repository.auth.ILoginHistoryRepository
import com.wayapaychat.domain.repository.payment.PaymentRepository
import com.wayapaychat.domain.repository.profile.IProfileRepository
import com.wayapaychat.domain.repository.profile.ProfileRepository
import com.wayapaychat.domain.repository.wallet.IWalletRepository
import com.wayapaychat.repository.impl.auth.IAuthRepositoryImpl
import com.wayapaychat.repository.impl.auth.LoginHistoryRepositoryImpl
import com.wayapaychat.repository.impl.payment.PaymentRepositoryImpl
import com.wayapaychat.repository.impl.profile.IProfileRepositoryImpl
import com.wayapaychat.repository.impl.profile.ProfileRepositoryImpl
import com.wayapaychat.repository.impl.samples.CancelReasonsRepositoryImpl
import com.wayapaychat.repository.impl.samples.ManualRideRepositoryImpl
import com.wayapaychat.repository.impl.wallet.WalletRepositoryImpl
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import com.wayapaychat.repository.mappers.samples.CancelReasonsEntityMapper
import com.wayapaychat.repository.mappers.samples.FareCalculatorEntityMapper
import com.wayapaychat.repository.remote.auth.IAuthRemote
import com.wayapaychat.repository.remote.auth.ILoginHistoryRemote
import com.wayapaychat.repository.remote.payment.IPaymentRemote
import com.wayapaychat.repository.remote.profile.IProfileRemote
import com.wayapaychat.repository.remote.profile.ProfileRemote
import com.wayapaychat.repository.remote.sample.CancelReasonsRemote
import com.wayapaychat.repository.remote.sample.ManualRideRemote
import com.wayapaychat.repository.remote.wallet.IWalletRemote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WayaAppRepositoryModule {

    @Provides
    @Singleton
    fun bindDriverDetailsRepository(
        manualRideDetailsRemote: ManualRideRemote,
        fareCalculatorEntityMapper: FareCalculatorEntityMapper
    ): IDriverDetailsRepository =
        ManualRideRepositoryImpl(manualRideDetailsRemote, fareCalculatorEntityMapper)

    @Provides
    @Singleton
    fun bindCancelReasonsRepository(
        cancelReasonsRemote: CancelReasonsRemote,
        cancelReasonsEntityMapper: CancelReasonsEntityMapper,
    ): CancelReasonsRepository =
        CancelReasonsRepositoryImpl(cancelReasonsRemote, cancelReasonsEntityMapper)

    @Provides
    @Singleton
    fun bindRiderRepository(
        authRemote: IAuthRemote,
        iAuthenticationLocal: IAuthenticationLocal,
        iWayaPreferenceRepository: IWayaPreferenceRepository
    ): AuthRepository =
        IAuthRepositoryImpl(authRemote, iAuthenticationLocal, iWayaPreferenceRepository)

    @Provides
    @Singleton
    fun bindPaymentRepository(
        paymentRemote: IPaymentRemote,
        iAuthenticationLocal: IAuthenticationLocal,
    ): PaymentRepository = PaymentRepositoryImpl(paymentRemote, iAuthenticationLocal)

    @Provides
    @Singleton
    fun bindProfileRepository(
        profileRemote: IProfileRemote,
        iAuthenticationLocal: IAuthenticationLocal,
        wayaPreferenceRepository: IWayaPreferenceRepository
    ): IProfileRepository = ProfileRepositoryImpl(profileRemote, iAuthenticationLocal, wayaPreferenceRepository)

    @Provides
    @Singleton
    fun bindAuthProfileRepository(
        profileRemote: ProfileRemote,
    ): ProfileRepository = IProfileRepositoryImpl(profileRemote)

    @Provides
    @Singleton
    fun bindLoginHistoryRepository(
        authenticationLocal: IAuthenticationLocal,
        loginHistoryRemote: ILoginHistoryRemote,
        wayaPreferenceRepository: IWayaPreferenceRepository
    ): ILoginHistoryRepository = LoginHistoryRepositoryImpl(authenticationLocal, loginHistoryRemote, wayaPreferenceRepository)

    @Provides
    @Singleton
    fun bindWalletRepository(
        authenticationLocal: IAuthenticationLocal,
        walletRemote: IWalletRemote
    ): IWalletRepository = WalletRepositoryImpl(authenticationLocal, walletRemote)
}
