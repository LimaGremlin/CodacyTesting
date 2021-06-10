package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class GetUserBankCardsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithNoParamsNoFlow<BaseDomainModel<List<UserBankCard>>>() {

    override suspend fun buildUseCase(): BaseDomainModel<List<UserBankCard>> {
        return paymentRepository.getUserBankCards()
    }
}
