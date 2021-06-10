package com.wayapaychat.domain.interactors.wallet

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.wallet.WalletDomainModel
import com.wayapaychat.domain.repository.wallet.IWalletRepository
import javax.inject.Inject

class GetWalletsUseCase @Inject constructor(
    private val walletRepository: IWalletRepository
) : UseCaseWithNoParamsNoFlow<BaseDomainModel<List<WalletDomainModel>>>() {

    override suspend fun buildUseCase(): BaseDomainModel<List<WalletDomainModel>> =
        walletRepository.getWallets()
}
