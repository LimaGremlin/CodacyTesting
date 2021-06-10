package com.wayapaychat.domain.interactors.payment

import com.wayapaychat.domain.interactors.UseCaseWithNoParamsSuspendNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.BankAccountDomainModel
import com.wayapaychat.domain.repository.payment.PaymentRepository
import javax.inject.Inject

class GetBankAccountsUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) : UseCaseWithNoParamsSuspendNoFlow<BaseDomainModel<List<BankAccountDomainModel>>>() {

    override suspend fun buildUseCase(): BaseDomainModel<List<BankAccountDomainModel>> =
        paymentRepository.getBankAccounts()

}
