package com.wayapaychat.repository.remote.payment

import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.payment.*

interface IPaymentRemote {

    suspend fun sendOTPToAssignQRCode(phoneNumber: String)

    suspend fun assignQRCode(request: HashMap<String, Any>)

    suspend fun getUserBankCards(): BaseRepositoryModel<List<UserBankCardDataEntity>>

    suspend fun deleteBankCard(cardNumber: String): BaseRepositoryModel<Boolean>

    suspend fun addBankCard(request: HashMap<String, Any>): BaseRepositoryModel<AddCardEntity>

    suspend fun submitOtp(reference: String, otp: String): BaseRepositoryModel<Boolean>

    suspend fun submitPhoneNumber(reference: String, phoneNumber: String): BaseRepositoryModel<Boolean>

    suspend fun addBankAccount(request: HashMap<String, String>): BaseRepositoryModel<Nothing>

    suspend fun getBanks(): BaseRepositoryModel<List<BankEntity>>

    suspend fun resolveAccountNumber(request: HashMap<String, String>): BaseRepositoryModel<ResolveAccountNumberResponseEntity>

    suspend fun getBankAccounts(): BaseRepositoryModel<List<BankAccountEntity>>

    suspend fun deleteBankAccount(accountNumber: String): BaseRepositoryModel<Nothing>

}
