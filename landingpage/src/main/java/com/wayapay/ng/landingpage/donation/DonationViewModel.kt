package com.wayapay.ng.landingpage.donation

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapay.ng.landingpage.R
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.servicesutils.WayaDonation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//ToDo Donation Reminder: Server do not have variable to display-contribution or not
class DonationViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    private val wayaGramSqLiteDB = WayaDatabase.invoke(activity.applicationContext).getWayaGramDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var headerText: MutableLiveData<String> = MutableLiveData()
    fun setHeaderText(text: String){headerText.value = text}
    fun getHeaderText():MutableLiveData<String> = headerText

    private var publishButtonText: MutableLiveData<String> = MutableLiveData(activity.getString(R.string.next))
    fun setPublishButtonText(text: String){publishButtonText.value = text}
    fun getPublishButtonText():MutableLiveData<String> = publishButtonText

    private var wayaGramUser: MutableLiveData<WayaGramUser> = MutableLiveData()
    private fun setWayaGramUser(value: WayaGramUser) {wayaGramUser.value = value }
    fun getWayaGramUser(): MutableLiveData<WayaGramUser> = wayaGramUser

    private var donation: MutableLiveData<WayaDonation> = MutableLiveData()
    fun setDonation(d: WayaDonation){donation.value = d}
    fun getDonation():MutableLiveData<WayaDonation> = donation

    fun getGramUserAsync(id: String) {
        uiScope.launch {
            val user = getGramUserFromDB(id) ?: WayaGramUser()
            setWayaGramUser(user)
        }
    }

    private suspend fun getGramUserFromDB(id: String): WayaGramUser?{
        return wayaGramSqLiteDB.getUserByAuthId(id)
    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}