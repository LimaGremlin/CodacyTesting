package com.wayapay.ng.landingpage.group

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.models.WayaGroup

class GroupViewModel(private val activity: Activity, application: Application): AndroidViewModel(application){

    private var buttonText: MutableLiveData<String> = MutableLiveData(activity.getString(R.string.continue_))
    fun setButtonText(text: String){buttonText.value = text}
    fun getButtonText(): MutableLiveData<String> = buttonText

    private var wayaGroup: MutableLiveData<WayaGroup> = MutableLiveData()
    fun getWayaGroup(): MutableLiveData<WayaGroup> = wayaGroup
    fun setWayaGroup(group: WayaGroup){wayaGroup.value = group}
}