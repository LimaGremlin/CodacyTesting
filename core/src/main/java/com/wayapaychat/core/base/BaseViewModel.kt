package com.wayapaychat.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(): ViewModel() {

    private val _navigationCommand = MutableLiveData<WayaNavigationCommand>()
    val navigationCommand = _navigationCommand as LiveData<WayaNavigationCommand>

    fun navigate(command: WayaNavigationCommand) {
        _navigationCommand.postValue(
            command
        )
    }
}
