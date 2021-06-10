package com.wayapaychat.ng.payment.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.VerifyLoggedInUserPinUseCase
import com.wayapaychat.domain.interactors.payment.GetBankAccountsUseCase
import com.wayapaychat.ng.payment.mappers.toPresentation
import com.wayapaychat.ng.payment.model.BankAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ManageBankAccountViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var getBankAccountsUseCase: GetBankAccountsUseCase

    private val _getBankAccountsLiveData = MutableLiveData<WayaAppResource<List<BankAccount>>>()
    val getBankAccountsLiveData: LiveData<WayaAppResource<List<BankAccount>>>
        get() = _getBankAccountsLiveData

    fun getBankAccounts(){
        viewModelScope.launch {
            _getBankAccountsLiveData.value = WayaAppResource.loading()

            try {
                val response = getBankAccountsUseCase.buildUseCase()

                if (response.successful){
                    _getBankAccountsLiveData.value = WayaAppResource.success(response.data?.map { it.toPresentation() })
                }else{
                    _getBankAccountsLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _getBankAccountsLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

}