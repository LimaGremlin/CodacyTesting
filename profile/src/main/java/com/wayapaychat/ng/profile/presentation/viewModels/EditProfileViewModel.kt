package com.wayapaychat.ng.profile.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.domain.interactors.profile.GetProfileDetailsUseCase
import com.wayapaychat.domain.interactors.profile.UpdateCorporateProfileUseCase
import com.wayapaychat.domain.interactors.profile.UpdatePersonalProfileUseCase
import com.wayapaychat.domain.interactors.profile.UpdateProfilePictureUseCase
import com.wayapaychat.ng.profile.presentation.mappers.toPresentation
import com.wayapaychat.ng.profile.presentation.model.profile.EditCorporateProfileRequest
import com.wayapaychat.ng.profile.presentation.model.profile.EditPersonalProfileRequest
import com.wayapaychat.ng.profile.presentation.model.profile.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel  @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var updatePersonalProfileUseCase: UpdatePersonalProfileUseCase

    @Inject
    lateinit var updateProfilePictureUseCase: UpdateProfilePictureUseCase

    @Inject
    lateinit var updateCorporateProfileUseCase: UpdateCorporateProfileUseCase

    @Inject
    lateinit var getProfileDetailsUseCase: GetProfileDetailsUseCase

    private var _editProfileLiveData = MutableLiveData<WayaAppResource<Nothing?>>()
    val editProfileLiveData: LiveData<WayaAppResource<Nothing?>> get() = _editProfileLiveData

    private var _updateProfilePictureLiveData = MutableLiveData<WayaAppResource<String>>()
    val updateProfilePictureLiveData: LiveData<WayaAppResource<String>> get() = _updateProfilePictureLiveData

    private var _profileDetailsLiveData = MutableLiveData<WayaAppResource<User>>()
    val profileDetailsLiveData: LiveData<WayaAppResource<User>> get() = _profileDetailsLiveData

    fun editPersonalProfile(request: EditPersonalProfileRequest){
        _editProfileLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try {
                val response = updatePersonalProfileUseCase
                    .buildUseCase(
                        with(request){
                            UpdatePersonalProfileUseCase.Parameter.make(
                                address,
                                dateOfBirth,
                                district,
                                email,
                                firstName,
                                gender,
                                middleName,
                                phoneNumber,
                                surname
                            )
                        }
                    )
                if (response.successful){
                    _editProfileLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _editProfileLiveData.value = WayaAppResource.validationFailed(response.message)
                }
            }catch(e: Exception) {
                _editProfileLiveData.value = com.wayapaychat.core.utils.state.WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun updateProfilePicture(profilePictureFile: File){
        _updateProfilePictureLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try {
                val response = updateProfilePictureUseCase.buildUseCase(profilePictureFile)
                if (response.successful){
                    _updateProfilePictureLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _updateProfilePictureLiveData.value = WayaAppResource.failed(response.message)
                }
            }catch(e: Exception) {
                _updateProfilePictureLiveData.value = com.wayapaychat.core.utils.state.WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun editCorporateProfile(request: EditCorporateProfileRequest){
        _editProfileLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try {
                val response = updateCorporateProfileUseCase
                    .buildUseCase(
                        with(request){
                            UpdateCorporateProfileUseCase.Parameter.make(
                                address,
                                dateOfBirth,
                                district,
                                email,
                                firstName,
                                gender,
                                middleName,
                                phoneNumber,
                                surname,
                                businessType,
                                organisationName,
                                organisationType
                            )
                        }
                    )
                if (response.successful){
                    _editProfileLiveData.value = WayaAppResource.success(response.data)
                }else{
                    _editProfileLiveData.value = WayaAppResource.validationFailed(response.message)
                }
            }catch (e: Exception){
                _editProfileLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun getProfileDetails(reset: Boolean = false){
        _profileDetailsLiveData.value = WayaAppResource.loading()

        viewModelScope.launch {
            try {
                val response = getProfileDetailsUseCase.buildUseCase()
                if (response.successful){
                    _profileDetailsLiveData.value = WayaAppResource.success(response.data?.toPresentation())
                }else{
                    _profileDetailsLiveData.value = WayaAppResource.validationFailed(response.message)
                }
            }catch (e: Exception){
                _profileDetailsLiveData.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

}
