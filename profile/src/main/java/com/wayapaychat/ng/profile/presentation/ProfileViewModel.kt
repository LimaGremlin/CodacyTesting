package com.wayapaychat.ng.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.GetLoggedInUserDataUseCase
import com.wayapaychat.domain.interactors.profile.GetUserProfileUseCase
import com.wayapaychat.domain.models.auth.LoginUser
import com.wayapaychat.domain.models.profile.UserDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getLoggedInUserDataUseCase: GetLoggedInUserDataUseCase
) : BaseViewModel() {

    private val _userProfileDetailsLiveData = MutableLiveData<WayaAppResource<UserDomainModel>>()
    val userProfileDetailsLiveData: LiveData<WayaAppResource<UserDomainModel>>
        get() = _userProfileDetailsLiveData

    private val _userData = MutableLiveData<LoginUser>()

    init {
//        getUserDataFromDB()
    }

    private fun getUserDataFromDB() {
        _userProfileDetailsLiveData.postValue(WayaAppResource.loading())
        viewModelScope.launch {
            getLoggedInUserDataUseCase.buildUseCase().let {
                it.data?.let { loggedInUserData ->
                    _userData.postValue(loggedInUserData)
                    getUserProfile(loggedInUserData.userId)
                }
            }
        }
    }

    private fun getUserProfile(userId: Int) {
        _userProfileDetailsLiveData.postValue(WayaAppResource.loading())
        viewModelScope.launch {
            try{
                val response = getUserProfileUseCase.buildUseCase(
                    param =
                    GetUserProfileUseCase.Parameter.make(
                        userId
                    )
                )
                if (response.successful)
                    _userProfileDetailsLiveData.postValue(WayaAppResource.success(response.data))
                else
                    _userProfileDetailsLiveData.postValue(WayaAppResource.validationFailed(response.message))
            }catch(e: Exception) {
                _userProfileDetailsLiveData.postValue(WayaAppResource.failed("An Error Occurred"))
            }
        }
    }
}
