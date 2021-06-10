package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class SubmitOtpUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithParamsNoFlow<SubmitOtpUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val reference: String,
        val otp: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                reference: String,
                otp: String
            ): Parameter =
                Parameter(
                    reference,
                    otp
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Boolean> =
        param?.let {
            paymentRepository.submitOtp(it.reference, it.otp)
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
