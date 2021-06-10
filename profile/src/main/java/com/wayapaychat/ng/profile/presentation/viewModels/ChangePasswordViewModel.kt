package com.wayapaychat.ng.profile.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.profile.ChangePasswordUseCase
import com.wayapaychat.domain.interactors.profile.InitiateChangePasswordUseCase
import com.wayapaychat.ng.profile.presentation.model.profile.ChangePasswordRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
): BaseViewModel() {

    @Inject
    lateinit var initiateChangePasswordUseCase: InitiateChangePasswordUseCase
    @Inject
    lateinit var changePasswordUseCase: ChangePasswordUseCase

    private val _initiateChangePasswordLiveData = MutableLiveData<WayaAppResource<Nothing?>>()
    val initiateChangePasswordLiveData: LiveData<WayaAppResource<Nothing?>>
        get() = _initiateChangePasswordLiveData

    fun initiateChangePassword(){
        _initiateChangePasswordLiveData.value = WayaAppResource.loading()
        viewModelScope.launch {
            try {
                val response = initiateChangePasswordUseCase.buildUseCase()

                if (response.successful){
                    _initiateChangePasswordLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _initiateChangePasswordLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch(e: Exception) {
                _initiateChangePasswordLiveData.value = com.wayapaychat.core.utils.state.WayaAppResource.failed("An Error occurred")
            }

        }
    }

    private val _changePasswordLiveData = MutableLiveData<WayaAppResource<Nothing?>>()
    val changePasswordLiveData: LiveData<WayaAppResource<Nothing?>>
        get() = _changePasswordLiveData

    fun changePassword(request: ChangePasswordRequest){
        _changePasswordLiveData.value = WayaAppResource.loading()
        viewModelScope.launch {
            try {
                val response = changePasswordUseCase.buildUseCase(
                    with(request){
                        ChangePasswordUseCase.Parameter.make(
                            newPassword, oldPassword, otp
                        )
                    }
                )

                if (response.successful){
                    _changePasswordLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _changePasswordLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch(e: Exception) {
                _changePasswordLiveData.value = com.wayapaychat.core.utils.state.WayaAppResource.failed("An Error occurred")
            }
        }
    }

}