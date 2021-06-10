package com.wayapay.profile.qrcode

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wayapay.profile.qrcode.main.QrProfileViewModel

class QrProfileViewModelFactory (private val activity: Activity, private val application: Application): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(QrProfileViewModel::class.java) ->
                QrProfileViewModel(activity, application) as T
            else -> throw IllegalArgumentException("Unknown class")
        }
    }

}