package com.wayapaychat.ng.payment.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.GetLoggedInUserDataUseCase
import com.wayapaychat.domain.interactors.wallet.GetWalletsUseCase
import com.wayapaychat.ng.payment.mappers.toPresentation
import com.wayapaychat.ng.payment.model.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRCodeViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var getWalletsUseCase: GetWalletsUseCase

    @Inject
    lateinit var getLoggedInUserDataUseCase: GetLoggedInUserDataUseCase

    private val _getWalletsLiveData = MutableLiveData<WayaAppResource<List<Wallet>>>()
    val getWalletsLiveData: LiveData<WayaAppResource<List<Wallet>>>
        get() = _getWalletsLiveData

    private val _getLoggedInUserNameLiveData = MutableLiveData<String>()
    val getLoggedInUserNameLiveData: LiveData<String>
        get() = _getLoggedInUserNameLiveData

    fun getWallets(){
        _getWalletsLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try{
                val response = getWalletsUseCase.buildUseCase()
                if (response.successful){
                    _getWalletsLiveData.value = WayaAppResource.success(response.data?.map { it.toPresentation() })
                }else{
                    _getWalletsLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                e.printStackTrace()
                _getWalletsLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun getLoggedInUserName(){
        viewModelScope.launch {
            getLoggedInUserDataUseCase.buildUseCase().data?.let {
                _getLoggedInUserNameLiveData.value = "${it.firstName} ${it.lastName}"
            }
        }
    }

}