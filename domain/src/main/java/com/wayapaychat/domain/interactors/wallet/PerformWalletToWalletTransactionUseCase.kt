package com.wayapaychat.domain.interactors.wallet

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import com.wayapaychat.domain.repository.wallet.IWalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PerformWalletToWalletTransactionUseCase @Inject constructor(
    private val walletRepository: IWalletRepository
) : UseCaseWithParamsNoFlow<PerformWalletToWalletTransactionUseCase.Parameter, BaseDomainModel<Nothing>>() {

    data class Parameter(
        val amount: Int,
        val fromAccount: String,
        val toAccount: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                amount: Int,
                fromAccount: String,
                toAccount: String
            ): Parameter =
                Parameter(
                    amount,
                    fromAccount,
                    toAccount
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Nothing> =
        param?.let {
            walletRepository.performWalletToWalletTransaction(
                hashMapOf(
                    "amount" to it.amount,
                    "fromAccount" to it.fromAccount,
                    "toAccount" to it.toAccount
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
