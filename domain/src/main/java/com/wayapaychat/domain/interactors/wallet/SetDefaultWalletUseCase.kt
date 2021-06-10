package com.wayapaychat.domain.interactors.wallet

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import com.wayapaychat.domain.repository.wallet.IWalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetDefaultWalletUseCase @Inject constructor(
    private val walletRepository: IWalletRepository
) : UseCaseWithParamsNoFlow<String, BaseDomainModel<Nothing>>() {

    override suspend fun buildUseCase(param: String?): BaseDomainModel<Nothing> =
        param?.let {
            walletRepository.setDefaultWallet(
                param
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
