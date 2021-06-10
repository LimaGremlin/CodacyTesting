package com.wayapaychat.ng.wallet.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.VerifyLoggedInUserPinUseCase
import com.wayapaychat.domain.interactors.auth.VerifyUserPinUseCase
import com.wayapaychat.domain.interactors.profile.GetProfileDetailsUseCase
import com.wayapaychat.domain.interactors.profile.GetUserProfileUseCase
import com.wayapaychat.domain.interactors.wallet.CreateWalletUseCase
import com.wayapaychat.domain.interactors.wallet.GetWalletsUseCase
import com.wayapaychat.ng.wallet.mappers.toPresentation
import com.wayapaychat.ng.wallet.models.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WalletLandingViewModel @Inject constructor() : BaseViewModel(){

    @Inject
    lateinit var getWalletsUseCase: GetWalletsUseCase

    @Inject
    lateinit var verifyUserPinUseCase: VerifyLoggedInUserPinUseCase

    @Inject
    lateinit var createWalletUseCase: CreateWalletUseCase

    private val _getWalletsLiveData = MutableLiveData<WayaAppResource<List<Wallet>>>()
    val getWalletsLiveData: LiveData<WayaAppResource<List<Wallet>>>
        get() = _getWalletsLiveData

    private val _verifyUserPinLiveData = MutableLiveData<WayaAppResource<Boolean>>()
    val verifyUserPinLiveData: LiveData<WayaAppResource<Boolean>>
        get() = _verifyUserPinLiveData

    private val _createWalletLiveData = MutableLiveData<WayaAppResource<Wallet>>()
    val createWalletLiveData: LiveData<WayaAppResource<Wallet>>
        get() = _createWalletLiveData

    fun getWallets(){
        viewModelScope.launch {
            _getWalletsLiveData.value = WayaAppResource.loading()
            try {
                val response = getWalletsUseCase.buildUseCase()

                if (response.successful){
                    _getWalletsLiveData.value = WayaAppResource.success(response.data?.map { it.toPresentation() })
                }else{
                    _getWalletsLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _getWalletsLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

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

    fun createWallet(){
        viewModelScope.launch {
            _createWalletLiveData.value = WayaAppResource.loading()
            try {
                val response = createWalletUseCase.buildUseCase()

                if (response.successful){
                    _createWalletLiveData.value = WayaAppResource.success(response.data?.toPresentation())
                }else{
                    _createWalletLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _createWalletLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

}