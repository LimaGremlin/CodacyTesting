package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class SubmitPhoneNumberUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithParamsNoFlow<SubmitPhoneNumberUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val reference: String,
        val phoneNumber: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                reference: String,
                phoneNumber: String
            ): Parameter =
                Parameter(
                    reference,
                    phoneNumber
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Boolean> =
        param?.let {
            paymentRepository.submitPhoneNumber(it.reference, it.phoneNumber)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
