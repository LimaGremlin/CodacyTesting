package com.wayapaychat.ng.profile.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.profile.GetProfileDetailsUseCase
import com.wayapaychat.domain.interactors.profile.GetReferralCodeUseCase
import com.wayapaychat.domain.interactors.profile.GetUserProfileUseCase
import com.wayapaychat.ng.profile.presentation.mappers.toPresentation
import com.wayapaychat.ng.profile.presentation.model.profile.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileLandingViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var getReferralCodeUseCase: GetReferralCodeUseCase

    @Inject
    lateinit var getProfileDetailsUseCase: GetProfileDetailsUseCase

    private var _getReferralCodeLiveData = MutableLiveData<WayaAppResource<String>>()
    val getReferralCodeLiveData: LiveData<WayaAppResource<String>> get() = _getReferralCodeLiveData

    private var _profileDetailsLiveData = MutableLiveData<WayaAppResource<User>>()
    val profileDetailsLiveData: LiveData<WayaAppResource<User>> get() = _profileDetailsLiveData

    fun getReferralCode(){
        _getReferralCodeLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try{
                val response = getReferralCodeUseCase.buildUseCase()
                if (response.successful){
                    _getReferralCodeLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _getReferralCodeLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch (e: Exception){
                _getReferralCodeLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun getProfileDetails() {
        _profileDetailsLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try{
                val it = getProfileDetailsUseCase.buildUseCase()
                if (it.successful)
                    _profileDetailsLiveData.postValue(WayaAppResource.success(it.data?.toPresentation()))
                else
                    _profileDetailsLiveData.postValue(WayaAppResource.validationFailed(it.message))
            }catch (e: Exception){
                _profileDetailsLiveData.postValue(WayaAppResource.failed("An Error Occurred"))
            }
        }
    }
}