package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsSuspendNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.BankDomainModel
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.domain.repository.payment.PaymentRepository
import com.wayapaychat.domain.repository.profile.IProfileRepository
import javax.inject.Inject

class GetBanksUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithNoParamsSuspendNoFlow<BaseDomainModel<List<BankDomainModel>>>() {

    override suspend fun buildUseCase(): BaseDomainModel<List<BankDomainModel>> =
        paymentRepository.getBanks()

}
