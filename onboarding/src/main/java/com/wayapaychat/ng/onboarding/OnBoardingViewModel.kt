package com.wayapaychat.ng.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.domain.interactors.onboarding.GetOnBoardingStatusUseCase
import com.wayapaychat.domain.interactors.onboarding.SaveOnBoardingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val getOnBoardingStatusUseCase: GetOnBoardingStatusUseCase
) : BaseViewModel() {

    @Inject
    lateinit var saveOnBoardingStatusUseCase: SaveOnBoardingStatusUseCase

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

    fun setCompleteOnBoarding() {
        viewModelScope.launch {
            saveOnBoardingStatusUseCase.buildUseCase(true).collect {}
        }
    }

}
