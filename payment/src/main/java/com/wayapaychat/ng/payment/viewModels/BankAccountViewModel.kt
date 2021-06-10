package com.wayapaychat.ng.payment.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.payment.DeleteBankAccountUseCase
import com.wayapaychat.ng.payment.model.BankAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BankAccountViewModel @Inject constructor() : BaseViewModel(){

    @Inject
    lateinit var deleteBankAccountUseCase: DeleteBankAccountUseCase

    private val _bankAccountLiveData = MutableLiveData<BankAccount>()
    val bankAccountLiveData: LiveData<BankAccount>
        get() = _bankAccountLiveData

    private val _deleteBankAccountLiveData = MutableLiveData<WayaAppResource<Nothing>>()
    val deleteBankAccountLiveData: LiveData<WayaAppResource<Nothing>>
        get() = _deleteBankAccountLiveData

    fun setBankAccount(bankAccount: BankAccount){
        _bankAccountLiveData.value = bankAccount
    }

    fun deleteBankAccount(accountNumber: String){
        viewModelScope.launch {
            _deleteBankAccountLiveData.value = WayaAppResource.loading()

            try {
                val response = deleteBankAccountUseCase.buildUseCase(
                        DeleteBankAccountUseCase.Parameter(
                                accountNumber
                        )
                )

                if (response.successful){
                    _deleteBankAccountLiveData.value = WayaAppResource.success()
                }else{
                    _deleteBankAccountLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                e.printStackTrace()
                _deleteBankAccountLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

}