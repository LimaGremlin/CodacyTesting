package com.wayapaychat.ng.profile.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.profile.GetProfileDetailsUseCase
import com.wayapaychat.domain.interactors.profile.InitiateChangePasswordUseCase
import com.wayapaychat.ng.profile.presentation.mappers.toPresentation
import com.wayapaychat.ng.profile.presentation.model.profile.User
import com.wayapaychat.remote.base.BaseNetworkModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SendOtpForChangePasswordViewModel @Inject constructor(
): BaseViewModel() {

    @Inject
    lateinit var initiateChangePasswordUseCase: InitiateChangePasswordUseCase

    @Inject
    lateinit var getProfileDetailsUseCase: GetProfileDetailsUseCase

    private val _initiateChangePasswordLiveData = MutableLiveData<WayaAppResource<Nothing?>>()
    val initiateChangePasswordLiveData: LiveData<WayaAppResource<Nothing?>>
        get() = _initiateChangePasswordLiveData

    private var _profileDetailsLiveData = MutableLiveData<WayaAppResource<User>>()
    val profileDetailsLiveData: LiveData<WayaAppResource<User>> get() = _profileDetailsLiveData

    fun initiateChangePassword(){
        _initiateChangePasswordLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try{
                val response = initiateChangePasswordUseCase.buildUseCase()
                if (response.successful){
                    _initiateChangePasswordLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _initiateChangePasswordLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _initiateChangePasswordLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun getProfileDetails() {
        viewModelScope.launch {
            try {
                val response = getProfileDetailsUseCase.buildUseCase()
                if (response.successful)
                    _profileDetailsLiveData.postValue(WayaAppResource.success(response.data?.toPresentation()))
                else
                    _profileDetailsLiveData.postValue(WayaAppResource.validationFailed(response.message))
            }catch(e: Exception) {
            _profileDetailsLiveData.postValue(WayaAppResource.failed("An Error Occurred"))
            }
        }
    }

}