package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.AddCard
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class AddBankAccountUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithParamsNoFlow<AddBankAccountUseCase.Parameter, BaseDomainModel<Nothing>>() {

    data class Parameter(
        val accountName: String,
        val accountNumber: String,
        val bankCode: String,
        val bankName: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                accountName: String,
                accountNumber: String,
                bankCode: String,
                bankName: String
            ): Parameter =
                Parameter(
                    accountName,
                    accountNumber,
                    bankCode,
                    bankName
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Nothing> =
        param?.let {
            paymentRepository.addBankAccount(hashMapOf(
                "accountName" to it.accountName,
                "accountNumber" to it.accountNumber,
                "bankCode" to it.bankCode,
                "bankName" to it.bankName
            ))
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
