package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.AddCard
import com.wayapaychat.domain.models.payment.ResolveAccountNumberResponseDomainModel
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class ResolveAccountNumberUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithParamsNoFlow<ResolveAccountNumberUseCase.Parameter, BaseDomainModel<ResolveAccountNumberResponseDomainModel>>() {

    data class Parameter(
        val accountNumber: String,
        val bankCode: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                accountNumber: String,
                bankCode: String
            ): Parameter =
                Parameter(
                    accountNumber,
                    bankCode
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<ResolveAccountNumberResponseDomainModel> =
        param?.let {
            paymentRepository.resolveAccountNumber(hashMapOf(
                "accountNumber" to it.accountNumber,
                "bankCode" to it.bankCode
            ))
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
