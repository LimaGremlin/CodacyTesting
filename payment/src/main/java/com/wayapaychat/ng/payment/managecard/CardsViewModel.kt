package com.wayapaychat.ng.payment.managecard

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapaychat.core.newtwork.getCards
import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.remote.services.card.CardAPIService
import com.wayapaychat.remote.services.card.CardApi
import com.wayapaychat.remote.services.card.CardApi.retrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CardsViewModel (private val activity: Activity, application: Application): AndroidViewModel(application){

    private val sqLiteDB = WayaDatabase.invoke(activity.applicationContext).getUserDataDao()
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var list_card = mutableListOf<UserBankCard>()
    private var listCards: MutableLiveData<List<UserBankCard>> = MutableLiveData()
    fun setListCards(cards: List<UserBankCard>){listCards.value = cards}
    fun getListCards(): MutableLiveData<List<UserBankCard>> = listCards

    private var userData: MutableLiveData<UserDataLocalModel> = MutableLiveData()
    fun getUserData(): MutableLiveData<UserDataLocalModel> = userData
    private fun setUserData(data: UserDataLocalModel){userData.value = data}

    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getProgressBarVisibility(): MutableLiveData<Boolean> = progressBarVisibility
    fun setProgressBarVisibility(boolean: Boolean){progressBarVisibility.value = boolean}

    fun getUserRoom() {
        uiScope.launch {
            getAuthUserDataFromDB()
        }
    }
    private suspend fun getAuthUserDataFromDB() {
        val user = sqLiteDB.getUserLoginData()
        setUserData(user)
    }

    fun getListUsersCard(authHeader: String){
        list_card.clear()
        uiScope.launch {
            val propertiesDifferConfig = CardApi.retrofitService.getAllCardsAsync(authHeader)
            setProgressBarVisibility(false)
            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getCards()
                    list_card.add(page)
                }
                setListCards(list_card)
            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}