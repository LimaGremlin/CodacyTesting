package com.wayapaychat.ng.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wayapaychat.core.models.LogInInfo
import com.wayapaychat.core.newtwork.LogInHistoryApi
import com.wayapaychat.core.newtwork.getLogInInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginInformationViewModel : ViewModel() {

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var logInHistory: MutableLiveData<LogInInfo> = MutableLiveData()
    fun setLogInHistory(info: LogInInfo){logInHistory.value = info}
    fun getLogInHistory():MutableLiveData<LogInInfo> = logInHistory

    fun getLoginHistoryAsync(userId: Int){
        uiScope.launch {
            val propertiesDifferConfig = LogInHistoryApi.retrofitService.getLastKnownHistoryAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                Log.d("waya_gram", "Data = $data")

                /**
                 * NOTE!! All stings values gotten from server comes with double quote.
                 * You are to remove those quiotes
                 */
                val info = data.getLogInInfo()
                setLogInHistory(info)

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

                setLogInHistory(LogInInfo())

            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}