package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class DeleteBankCardUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithParamsNoFlow<DeleteBankCardUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val cardNumber: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                cardNumber: String
            ): Parameter =
                Parameter(
                    cardNumber
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Boolean> =
        param?.let {
            paymentRepository.deleteBankCard(cardNumber = it.cardNumber)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
