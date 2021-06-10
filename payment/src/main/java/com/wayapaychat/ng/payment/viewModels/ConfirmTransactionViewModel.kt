package com.wayapaychat.ng.payment.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.VerifyLoggedInUserPinUseCase
import com.wayapaychat.domain.interactors.wallet.PerformWalletToWalletTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ConfirmTransactionViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var verifyUserPinUseCase: VerifyLoggedInUserPinUseCase

    @Inject
    lateinit var performWalletToWalletTransactionUseCase: PerformWalletToWalletTransactionUseCase

    private val _verifyUserPinLiveData = MutableLiveData<WayaAppResource<Boolean>>()
    val verifyUserPinLiveData: LiveData<WayaAppResource<Boolean>>
        get() = _verifyUserPinLiveData

    private val _performWalletToWalletTransactionLiveData = MutableLiveData<WayaAppResource<Nothing>>()
    val performWalletToWalletTransactionLiveData: LiveData<WayaAppResource<Nothing>>
        get() = _performWalletToWalletTransactionLiveData

    fun verifyUserPin(pin: String){
        viewModelScope.launch {
            _verifyUserPinLiveData.value = WayaAppResource.loading()
            try {
                val response =
                    verifyUserPinUseCase.buildUseCase(
                        VerifyLoggedInUserPinUseCase.Parameter(
                            pin
                        )
                    )

                if (response.successful){
                    _verifyUserPinLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _verifyUserPinLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _verifyUserPinLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun performWalletToWalletTransaction(amount: Int, fromAccount: String, toAccount: String){
        viewModelScope.launch {
            _performWalletToWalletTransactionLiveData.value = WayaAppResource.loading()
            try {
                val response =
                    performWalletToWalletTransactionUseCase.buildUseCase(
                        PerformWalletToWalletTransactionUseCase.Parameter(
                            amount, fromAccount, toAccount
                        )
                    )

                if (response.successful){
                    _performWalletToWalletTransactionLiveData.value = WayaAppResource.success(null)
                }else{
                    _performWalletToWalletTransactionLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _performWalletToWalletTransactionLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

}