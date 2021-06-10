package com.wayapaychat.domain.repository.payment

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.*

interface PaymentRepository {
    suspend fun addCard(request: HashMap<String, Any>): BaseDomainModel<AddCard>

    suspend fun getUserBankCards(): BaseDomainModel<List<UserBankCard>>

    suspend fun deleteBankCard(cardNumber: String): BaseDomainModel<Boolean>

    suspend fun submitOtp(reference: String, otp: String): BaseDomainModel<Boolean>

    suspend fun submitPhoneNumber(reference: String, phoneNumber: String): BaseDomainModel<Boolean>

    suspend fun addBankAccount(request: HashMap<String, String>): BaseDomainModel<Nothing>

    suspend fun getBanks(): BaseDomainModel<List<BankDomainModel>>

    suspend fun resolveAccountNumber(request: HashMap<String, String>): BaseDomainModel<ResolveAccountNumberResponseDomainModel>

    suspend fun getBankAccounts(): BaseDomainModel<List<BankAccountDomainModel>>

    suspend fun deleteBankAccount(accountNumber: String): BaseDomainModel<Nothing>

}
