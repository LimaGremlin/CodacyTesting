package com.wayapaychat.ng.payment.managecard

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.wayapaychat.core.models.TempWallet
import com.wayapaychat.core.newtwork.WayaPayApi
import com.wayapaychat.core.newtwork.getAllCardsAsync
import com.wayapaychat.core.newtwork.getTempWallet
import com.wayapaychat.core.utils.Event
import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.domain.models.payment.UserBankCardDetails
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.model.AddCardRequest
import com.wayapaychat.remote.services.card.CardApi
import com.wayapaychat.remote.services.wallet.WalletApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManageCardViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    private val sqLiteDB = WayaDatabase.invoke(activity.applicationContext).getUserDataDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var wallet: MutableLiveData<TempWallet> = MutableLiveData()
    fun getWallet():MutableLiveData<TempWallet> = wallet
    fun setWallet(w: TempWallet) { wallet.value = w }
    private var list_wallets_ = mutableListOf<TempWallet>()
    private var listWallets: MutableLiveData<List<TempWallet>> = MutableLiveData()
    fun setListWallets(wallets: List<TempWallet>){listWallets.value = wallets}
    fun getListWallets():MutableLiveData<List<TempWallet>> = listWallets

    private var list_card = mutableListOf<UserBankCardDetails>()
    private var listCards: MutableLiveData<List<UserBankCardDetails>> = MutableLiveData()
    fun setListCards(cards: List<UserBankCardDetails>){listCards.value = cards}
    fun getListCards():MutableLiveData<List<UserBankCardDetails>> = listCards

    private var userData: MutableLiveData<UserDataLocalModel> = MutableLiveData()
    fun getUserData(): MutableLiveData<UserDataLocalModel> = userData
    private fun setUserData(data: UserDataLocalModel){userData.value = data}

    private var textHeader: MutableLiveData<String> = MutableLiveData()
    fun setTextHeader(text: String) { textHeader.value = text }
    fun getTextHeader():MutableLiveData<String> = textHeader

    private var addWalletVisibility: MutableLiveData<Boolean> = MutableLiveData(true)
    fun getAddWalletVisibility():MutableLiveData<Boolean> = addWalletVisibility
    fun setAddWalletVisibility(boolean: Boolean){addWalletVisibility.value = boolean}

    //

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

    fun getAllUsersWalletsAsync(userId: Int){
        //clear list wallet
        list_wallets_.clear()
        uiScope.launch {
            val propertiesDifferConfig = WalletApi.retrofitService.getAllUserWalletsAsync(userId)
            setProgressBarVisibility(false)
            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getTempWallet()
                    list_wallets_.add(page)
                }
                setListWallets(list_wallets_)
            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    fun getListUsersCard(authHeader: String){
        list_card.clear()
        uiScope.launch {
            val propertiesDifferConfig = CardApi.retrofitService.getAllCardsAsync(authHeader)
            setProgressBarVisibility(false)
            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("waya_card", "Card = $result")
                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getAllCardsAsync()
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


    fun addCard(map: HashMap<String, String>, authHeader: String, navController: NavController){
        setProgressBarVisibility(false)
        val user = getUserData().value ?: UserDataLocalModel()
        uiScope.launch {
            val propertiesDifferConfig = CardApi.retrofitService.addCardsAsync(authHeader, map)
            setProgressBarVisibility(false)
            try {
                val result = propertiesDifferConfig.await()
                Log.d("account_service", "result = $result")

               // getUserBanks(user.token)
                navController.navigate(R.id.action_newCardFragment_to_addCardSuccessFragment)

            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    fun verifyUserPinAsync(userPin: String, token: String, action: String){
        val user = getUserData().value ?: UserDataLocalModel()
        setProgressBarVisibility(true)
        uiScope.launch {

        }
//            val propertiesDifferConfig = WayaPayApi.retrofitService.validatePinAsync(userPin, token)
//
//            try {
//                val result = propertiesDifferConfig.await()
//                val body = result.body()
//                Log.d("create_pin", "Verify pin result = $result")
//
//                when(result.code()){
//                    200, 201 -> {
//                        when(action) {
//                            activity.getString(R.string.delete_bank_account)-> {
//                                //create new wallet for user on success
//                              //  deleteBankAccount(user.token)
//
//                            }
//                        }
//                    }
//                    else ->{
//                        setProgressBarVisibility(false)
//                        Toast.makeText(activity.applicationContext, activity.getString(R.string.invalid_pin), Toast.LENGTH_LONG).show()
//                    }
//                }
//            } catch (e: Exception) {
//                setProgressBarVisibility(false)
//                Toast.makeText(activity.applicationContext, activity.getString(R.string.error_verifying_pin), Toast.LENGTH_LONG).show()
//            }
//        }
    }


    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}