package com.wayapaychat.ng.payment.model

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapaychat.core.newtwork.getWayaGramUser
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.services.wayagram.GramProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PaymentViewModel(private val activity: Activity, application: Application) : AndroidViewModel(application){

    private val sqLiteDB = WayaDatabase.invoke(activity.applicationContext).getUserDataDao()
    private val wayaGramSqLiteDB = WayaDatabase.invoke(activity.applicationContext).getWayaGramDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var wayaGramUser: MutableLiveData<WayaGramUser> = MutableLiveData()
    private fun setWayaGramUser(value: WayaGramUser) {wayaGramUser.value = value }
    fun getWayaGramUser(): MutableLiveData<WayaGramUser> = wayaGramUser

    private var textHeader: MutableLiveData<String> = MutableLiveData()
    fun getTextHeader():MutableLiveData<String> = textHeader
    fun setTextHeader(text: String){textHeader.value = text}

    fun getGramUserAsync(id: String) {
        uiScope.launch {
            val user = getGramUserFromDB(id)
            if(user == null)getWayaProfileByID(id.toIntOrNull() ?: -1)
            else setWayaGramUser(user)
        }
    }

    private suspend fun getGramUserFromDB(id: String): WayaGramUser?{
        return wayaGramSqLiteDB.getUserByAuthId(id)
    }

    //Save WayaGram user In DB
    private suspend fun saveUserInDB(user: WayaGramUser){
        wayaGramSqLiteDB.insert(user)
    }

    private fun getWayaProfileByID(userId: Int){

        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.getProfileByUIAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")
                val userData = data.getAsJsonObject("user")

                /**
                 * NOTE!! All stings values gotten from server comes with double quote.
                 * You are to remove those quiotes
                 */
                val user = data.getWayaGramUser()
                setWayaGramUser(user)
                //save User Data in Room DB
                saveUserInDB(user)

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}
