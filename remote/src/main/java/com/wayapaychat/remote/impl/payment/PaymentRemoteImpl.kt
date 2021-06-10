package com.wayapaychat.remote.impl.payment

import com.wayapaychat.remote.mappers.payment.toAddCardEntity
import com.wayapaychat.remote.mappers.payment.toEntity
import com.wayapaychat.remote.mappers.payment.toUserBankCardDataEntity
import com.wayapaychat.remote.services.payment.CardService
import com.wayapaychat.remote.services.payment.QRCodeService
import com.wayapaychat.remote.utils.WayaAppExceptionHandler
import com.wayapaychat.remote.utils.WayaAppRemoteConstants
import com.wayapaychat.repository.models.BaseRepositoryModel
import com.wayapaychat.repository.models.payment.*
import com.wayapaychat.repository.remote.payment.IPaymentRemote
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class PaymentRemoteImpl @Inject constructor(
    @Named(value = WayaAppRemoteConstants.QR_CODE_RETROFIT) qrCodeRetrofit: Retrofit,
    @Named(value = WayaAppRemoteConstants.CARD_RETROFIT) cardRetrofit: Retrofit
) : IPaymentRemote {

    private val qrCodeService = qrCodeRetrofit.create(QRCodeService::class.java)
    private val cardService = cardRetrofit.create(CardService::class.java)

    override suspend fun sendOTPToAssignQRCode(phoneNumber: String) {
        qrCodeService.sendOTPToAssignQRCode(phoneNumber)
    }

    override suspend fun assignQRCode(request: HashMap<String, Any>) {
        qrCodeService.assignQRCode(request)
    }

    override suspend fun getUserBankCards(): BaseRepositoryModel<List<UserBankCardDataEntity>> {
        return try {
            val response = cardService.getUserBankCards()

            BaseRepositoryModel(
                response.success,
                response.data?.data?.map {
                    it.toUserBankCardDataEntity()
                },
                response.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }
    }

    override suspend fun deleteBankCard(cardNumber: String): BaseRepositoryModel<Boolean> {
        return try {
            val response = cardService.deleteBankCard(cardNumber)

            BaseRepositoryModel(
                response.success,
                response.data?.status,
                response.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }
    }

    override suspend fun addBankCard(request: HashMap<String, Any>): BaseRepositoryModel<AddCardEntity> {
        return try {
            val response = cardService.addBankCard(requestBody = request)

            BaseRepositoryModel(
                response.data?.status!!,
                response.data.addCardDataModel?.toAddCardEntity(),
                response.data.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }
    }

    override suspend fun submitOtp(
        reference: String,
        otp: String
    ): BaseRepositoryModel<Boolean> {
        return try {
            val response = cardService.submitOTP(otp, reference)

            BaseRepositoryModel(
                response.data?.status!!,
                response.data.status,
                response.data.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }
    }

    override suspend fun submitPhoneNumber(reference: String, phoneNumber: String): BaseRepositoryModel<Boolean> {
        return try {
            val response = cardService.submitPhoneNumber(phoneNumber, reference)

            BaseRepositoryModel(
                response.data?.status!!,
                response.data.status,
                response.data.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }
    }

    override suspend fun addBankAccount(request: HashMap<String, String>): BaseRepositoryModel<Nothing> =
        try {
            val response = cardService.addBankAccount(request)

            BaseRepositoryModel(
                response.data?.status ?: response.success,
                null,
                response.data?.message ?: response.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }

    override suspend fun getBanks(): BaseRepositoryModel<List<BankEntity>> =
        try {
            val response = cardService.getBanks()

            BaseRepositoryModel(
                response.data?.status ?: response.success,
                response.data?.data?.map { it.toEntity() },
                response.data?.message ?: response.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }

    override suspend fun resolveAccountNumber(request: HashMap<String, String>): BaseRepositoryModel<ResolveAccountNumberResponseEntity> =
        try {
            val response = cardService.resolveAccountNumber(request)

            BaseRepositoryModel(
                response.data?.status ?: response.success,
                response.data?.data?.toEntity(),
                response.data?.message ?: response.message
            )
        } catch (throwable: Throwable) {
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }

    override suspend fun getBankAccounts(): BaseRepositoryModel<List<BankAccountEntity>> =
        try{
            val response
                    = cardService.getBankAccounts()

            BaseRepositoryModel(
                response.data?.status ?: response.success,
                response.data?.data?.map{ it.toEntity() },
                response.data?.message ?: response.message
            )
        } catch (throwable: Throwable){
            BaseRepositoryModel(
                false,
                null,
                WayaAppExceptionHandler.getErrorFromThrowable(throwable)
            )
        }

    override suspend fun deleteBankAccount(accountNumber: String): BaseRepositoryModel<Nothing> =
            try{
                val response
                        = cardService.deleteBankAccount(accountNumber)

                BaseRepositoryModel(
                        response.data?.status ?: response.success,
                        null,
                        response.data?.message ?: response.message
                )
            } catch (throwable: Throwable){
                throwable.printStackTrace()
                BaseRepositoryModel(
                        false,
                        null,
                        WayaAppExceptionHandler.getErrorFromThrowable(throwable)
                )
            }

}
