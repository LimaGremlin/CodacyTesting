package com.wayapaychat.domain.interactors.wallet

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsSuspendNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.domain.models.wallet.WalletDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import com.wayapaychat.domain.repository.wallet.IWalletRepository
import javax.inject.Inject

class CreateWalletUseCase @Inject constructor(
    private val walletRepository: IWalletRepository
) : UseCaseWithNoParamsSuspendNoFlow<BaseDomainModel<WalletDomainModel>>() {

    override suspend fun buildUseCase(): BaseDomainModel<WalletDomainModel> =
        walletRepository.createWallet()

}
