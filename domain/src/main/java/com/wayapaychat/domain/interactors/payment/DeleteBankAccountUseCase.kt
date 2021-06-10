package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class DeleteBankAccountUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithParamsNoFlow<DeleteBankAccountUseCase.Parameter, BaseDomainModel<Nothing>>() {

    data class Parameter(
        val accountNumber: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                    accountNumber: String
            ): Parameter =
                Parameter(
                    accountNumber
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Nothing> =
        param?.let {
            paymentRepository.deleteBankAccount(it.accountNumber)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
