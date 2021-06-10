package com.wayapaychat.repository.impl.payment

import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.payment.*
import com.wayapaychat.domain.repository.payment.PaymentRepository
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.mappers.payment.toAddCard
import com.wayapaychat.repository.mappers.payment.toDomain
import com.wayapaychat.repository.mappers.payment.toUserBankCard
import com.wayapaychat.repository.remote.payment.IPaymentRemote

class PaymentRepositoryImpl(
    private val paymentRemote: IPaymentRemote,
    private val iAuthenticationLocal: IAuthenticationLocal
) : PaymentRepository {
    override suspend fun addCard(request: HashMap<String, Any>): BaseDomainModel<AddCard> {
        val response = paymentRemote.addBankCard(request)

        return BaseDomainModel(
            response.successful,
            response.data?.toAddCard(),
            response.message
        )
    }

    override suspend fun getUserBankCards(): BaseDomainModel<List<UserBankCard>> {
        val response = paymentRemote.getUserBankCards()
        return BaseDomainModel(
            response.successful,
            response.data?.map {
                it.toUserBankCard()
            },
            response.message
        )
    }

    override suspend fun deleteBankCard(cardNumber: String): BaseDomainModel<Boolean> {
        val response = paymentRemote.deleteBankCard(cardNumber)

        return BaseDomainModel(
            response.successful,
            response.data,
            response.message
        )
    }

    override suspend fun submitOtp(reference: String, otp: String): BaseDomainModel<Boolean> {
        val response = paymentRemote.submitOtp(reference, otp)

        return BaseDomainModel(
            response.successful,
            response.data,
            response.message
        )
    }

    override suspend fun submitPhoneNumber(reference: String, phoneNumber: String): BaseDomainModel<Boolean> {
        val response = paymentRemote.submitPhoneNumber(reference, phoneNumber)

        return BaseDomainModel(
            response.successful,
            response.data,
            response.message
        )
    }

    override suspend fun addBankAccount(request: HashMap<String, String>): BaseDomainModel<Nothing> {
        val userId = iAuthenticationLocal.getSavedUserData().user.id
        request.put("userId", userId.toString())
        val response = paymentRemote.addBankAccount(request)

        return BaseDomainModel(
            response.successful,
            response.data,
            response.message
        )
    }

    override suspend fun getBanks(): BaseDomainModel<List<BankDomainModel>> {
       val response = paymentRemote.getBanks()

        return BaseDomainModel(
            response.successful,
            response.data?.map { it.toDomain() },
            response.message
        )
    }

    override suspend fun resolveAccountNumber(request: HashMap<String, String>): BaseDomainModel<ResolveAccountNumberResponseDomainModel> {
        val response = paymentRemote.resolveAccountNumber(request)

        return BaseDomainModel(
            response.successful,
            response.data?.toDomain(),
            response.message
        )
    }

    override suspend fun getBankAccounts(): BaseDomainModel<List<BankAccountDomainModel>> {
        val response = paymentRemote.getBankAccounts()

        return BaseDomainModel(
            response.successful,
            response.data?.map { it.toDomain() },
            response.message
        )
    }

    override suspend fun deleteBankAccount(accountNumber: String): BaseDomainModel<Nothing> {
        val response = paymentRemote.deleteBankAccount(accountNumber)

        return BaseDomainModel(
                response.successful,
                response.data,
                response.message
        )
    }

}
