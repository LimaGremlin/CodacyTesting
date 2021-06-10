package com.wayapaychat.ng.wallet.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.wallet.SetDefaultWalletUseCase
import com.wayapaychat.ng.wallet.mappers.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WalletFragmentViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var setDefaultWalletUseCase: SetDefaultWalletUseCase

    private val _setDefaultWalletLiveData = MutableLiveData<WayaAppResource<Nothing>>()
    val setDefaultWalletLiveData: LiveData<WayaAppResource<Nothing>>
        get() = _setDefaultWalletLiveData

    fun setDefaultWallet(accountNo: String){
        viewModelScope.launch {
            _setDefaultWalletLiveData.value = WayaAppResource.loading()
            try {
                val response = setDefaultWalletUseCase.buildUseCase(accountNo)

                if (response.successful){
                    _setDefaultWalletLiveData.value = WayaAppResource.success()
                }else{
                    _setDefaultWalletLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _setDefaultWalletLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

}