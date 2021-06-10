package com.wayapaychat.ng.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.domain.interactors.onboarding.GetOnBoardingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val getOnBoardingStatusUseCase: GetOnBoardingStatusUseCase
) : BaseViewModel() {

    private val _isUserLoggedInLiveData = MutableLiveData<Boolean>()
    val isUserLoggedInLiveData: LiveData<Boolean>
        get() = _isUserLoggedInLiveData

    private val _onBoardingStatus = MutableLiveData<Boolean>()
    val onBoardingStatus: LiveData<Boolean>
        get() = _onBoardingStatus

    init {
        viewModelScope.launch {
            getOnBoardingStatusUseCase.buildUseCase().collect {
                it.data?.let { onBoardingStatus ->
                    _onBoardingStatus.postValue(onBoardingStatus)
                }
            }
        }
    }
}
