package com.wayapay.ng.landingpage.payment

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapaychat.core.models.TempWallet

class PayViewModel (private val activity: Activity, application: Application): AndroidViewModel(application){

    private var textHeader: MutableLiveData<String> = MutableLiveData()
    fun getTextHeader():MutableLiveData<String> = textHeader
    fun setTextHeader(string: String){textHeader.value = string}

    private var wallet: MutableLiveData<TempWallet> = MutableLiveData()
    fun setWallet(mWallet: TempWallet){wallet.value = mWallet}
    fun getWallet():MutableLiveData<TempWallet> = wallet

}