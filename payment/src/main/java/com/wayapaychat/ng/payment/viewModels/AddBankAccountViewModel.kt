package com.wayapaychat.ng.payment.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.VerifyLoggedInUserPinUseCase
import com.wayapaychat.domain.interactors.payment.AddBankAccountUseCase
import com.wayapaychat.domain.interactors.payment.GetBanksUseCase
import com.wayapaychat.domain.interactors.payment.ResolveAccountNumberUseCase
import com.wayapaychat.ng.payment.mappers.toPresentation
import com.wayapaychat.ng.payment.model.Bank
import com.wayapaychat.ng.payment.model.ResolveAccountNumberResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddBankAccountViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var getBanksUseCase: GetBanksUseCase

    @Inject
    lateinit var addBankAccountUseCase: AddBankAccountUseCase

    @Inject
    lateinit var resolveAccountNumberUseCase: ResolveAccountNumberUseCase

    private val _getBanksLiveData = MutableLiveData<WayaAppResource<List<Bank>>>()
    val getBanksLiveData: LiveData<WayaAppResource<List<Bank>>>
        get() = _getBanksLiveData

    private val _addBankAccountLiveData = MutableLiveData<WayaAppResource<Nothing>>()
    val addBankAccountLiveData: LiveData<WayaAppResource<Nothing>>
        get() = _addBankAccountLiveData

    private val _resolveAccountNumberLiveData = MutableLiveData<WayaAppResource<ResolveAccountNumberResponse>>()
    val resolveAccountNumberLiveData: LiveData<WayaAppResource<ResolveAccountNumberResponse>>
        get() = _resolveAccountNumberLiveData

    fun getBanks(){
        _getBanksLiveData.value = WayaAppResource.loading()
        viewModelScope.launch {
            try {
                val response =
                    getBanksUseCase.buildUseCase()

                if (response.successful){
                    _getBanksLiveData.value = WayaAppResource.success(response.data?.map { it.toPresentation() })
                }else{
                    _getBanksLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _getBanksLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun addBankAccount(accountName: String, accountNumber: String, bankCode: String, bankName: String){
        _addBankAccountLiveData.value = WayaAppResource.loading()
        viewModelScope.launch {
            try {
                val response =
                    addBankAccountUseCase.buildUseCase(
                        AddBankAccountUseCase.Parameter(
                            accountName, accountNumber, bankCode, bankName
                        )
                    )

                if (response.successful){
                    _addBankAccountLiveData.value = WayaAppResource.success()
                }else{
                    _addBankAccountLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _addBankAccountLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun resolveAccountNumber(accountNumber: String, bankCode: String){
        _resolveAccountNumberLiveData.value = WayaAppResource.loading()
        viewModelScope.launch {
            try {
                val response =
                    resolveAccountNumberUseCase.buildUseCase(
                        ResolveAccountNumberUseCase.Parameter(
                            accountNumber, bankCode
                        )
                    )

                if (response.successful){
                    _resolveAccountNumberLiveData.value = WayaAppResource.success(response.data?.toPresentation())
                }else{
                    _resolveAccountNumberLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _resolveAccountNumberLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

}