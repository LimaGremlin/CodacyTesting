package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.AddCard
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class AddBankCardUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithParamsNoFlow<AddBankCardUseCase.Parameter, BaseDomainModel<AddCard>>() {

    data class Parameter(
        val cardNumber: String,
        val cvv: String,
        val email: String,
        val expiryMonth: String,
        val expiryYear: String,
        val last4digit: String,
        val name: String,
        val pin: String,
        val userId: String,
        val walletAccountNumber: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                cardNumber: String,
                cvv: String,
                email: String,
                expiryMonth: String,
                expiryYear: String,
                last4digit: String,
                name: String,
                pin: String,
                userId: String,
                walletAccountNumber: String
            ): Parameter =
                Parameter(
                    cardNumber,
                    cvv,
                    email,
                    expiryMonth,
                    expiryYear,
                    last4digit,
                    name,
                    pin,
                    userId,
                    walletAccountNumber
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<AddCard> =
        param?.let {
            paymentRepository.addCard(hashMapOf(
                "cardNumber" to it.cardNumber,
                "cvv" to it.cvv,
                "email" to it.email,
                "expiryMonth" to it.expiryMonth,
                "expiryYear" to it.expiryYear,
                "last4digit" to it.last4digit,
                "name" to it.name,
                "pin" to it.pin,
                "userId" to it.userId,
                "walletAccountNumber" to it.walletAccountNumber
            ))
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
