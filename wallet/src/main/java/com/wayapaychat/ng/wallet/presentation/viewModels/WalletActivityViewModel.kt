package com.wayapaychat.ng.wallet.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wayapaychat.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletActivityViewModel @Inject constructor(): BaseViewModel() {

    private val _walletSetAsDefaultLiveData = MutableLiveData<String>()
    val walletSetAsDefaultLiveData: LiveData<String>
        get() = _walletSetAsDefaultLiveData

    fun walletSetAsDefault(accountNo: String){
        _walletSetAsDefaultLiveData.value = accountNo
    }

}