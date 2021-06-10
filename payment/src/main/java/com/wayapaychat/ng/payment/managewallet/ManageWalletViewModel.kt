package com.wayapaychat.ng.payment.managewallet

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.wayapaychat.core.models.TempWallet
import com.wayapaychat.core.newtwork.WayaPayApi
import com.wayapaychat.core.newtwork.getTempWallet
import com.wayapaychat.core.payment.Wallet
import com.wayapaychat.core.payment.toWallet
import com.wayapaychat.core.payment.toWayaAccount
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.ng.payment.ManageWalletActivity
import com.wayapaychat.ng.payment.R
import com.wayapaychat.remote.services.wallet.TempWalletApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManageWalletViewModel (private val activity: Activity, application: Application): AndroidViewModel(application){

    private val sqLiteDB = WayaDatabase.invoke(activity.applicationContext).getUserDataDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var list_wallets_ = mutableListOf<Wallet>()
    private var list_temp_wallets_ = mutableListOf<TempWallet>()

    private var listWallets: MutableLiveData<List<Wallet>> = MutableLiveData()
    fun setListWallets(tempWallets: List<Wallet>){listWallets.value = tempWallets}
    fun getListWallets():MutableLiveData<List<Wallet>> = listWallets

    private var listTempWallets: MutableLiveData<List<TempWallet>> = MutableLiveData()
    fun setListTempWallets(tempWallets: List<TempWallet>){listTempWallets.value = tempWallets}
    fun getListTempWallets():MutableLiveData<List<TempWallet>> = listTempWallets

    private var wallet: MutableLiveData<Wallet> = MutableLiveData()
    fun setWallet(w: Wallet){wallet.value = w}
    fun getWallet():MutableLiveData<Wallet> = wallet

    private var tempWallet: MutableLiveData<TempWallet> = MutableLiveData()
    fun getTempWallet():MutableLiveData<TempWallet> = tempWallet
    fun setTempWallet(w: TempWallet) { tempWallet.value = w }

    private var textHeader: MutableLiveData<String> = MutableLiveData()
    fun setTextHeader(text: String) { textHeader.value = text }
    fun getTextHeader():MutableLiveData<String> = textHeader

    private var addWalletVisibility: MutableLiveData<Boolean> = MutableLiveData(true)
    fun getAddWalletVisibility():MutableLiveData<Boolean> = addWalletVisibility
    fun setAddWalletVisibility(boolean: Boolean){addWalletVisibility.value = boolean}

    private var optionsVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getOptionsVisibility(): MutableLiveData<Boolean> = optionsVisibility
    fun setOptionsVisibility(boolean: Boolean){optionsVisibility.value = boolean}

    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getProgressBarVisibility(): MutableLiveData<Boolean> = progressBarVisibility
    fun setProgressBarVisibility(boolean: Boolean){progressBarVisibility.value = boolean}

    private var userData: MutableLiveData<UserDataLocalModel> = MutableLiveData()
    fun getUserData(): MutableLiveData<UserDataLocalModel> = userData
    private fun setUserData(data: UserDataLocalModel){userData.value = data}

    //add a new item to sqlite db using room
    fun getUserRoom() {
        uiScope.launch {
            getAuthUserDataFromDB()
        }
    }
    private suspend fun getAuthUserDataFromDB() {
        val user = sqLiteDB.getUserLoginData()
        setUserData(user)
        getAllUsersWalletsAsync(user.id)
    }

    fun verifyUserPinAsync(userPin: String, token: String, navController: NavController, action: String){
        val user = getUserData().value ?: UserDataLocalModel()
        setProgressBarVisibility(true)
        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.validatePinAsync(userPin, token)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                Log.d("create_pin", "Verify pin result = $result")

                when(result.code()){
                    200, 201 -> {
                        when(action) {
                            activity.getString(R.string.create_new_wallet)-> {
                                //create new wallet for user on success
                                createNewWalletAsync(user.id.toString(), navController)
                            }
                            activity.getString(R.string.make_default_wallet) ->{
                                val wallet = getTempWallet().value ?: TempWallet()
                                makeDefaultWallet(user.id, wallet.accountNo)
                            }
                        }
                    }
                    else ->{
                        setProgressBarVisibility(false)
                        Toast.makeText(activity.applicationContext, activity.getString(R.string.invalid_pin), Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                setProgressBarVisibility(false)
                Toast.makeText(activity.applicationContext, activity.getString(R.string.error_verifying_pin), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun makeDefaultWallet(userId: Int, accName: String){
        uiScope.launch {
            val propertiesDifferConfig = TempWalletApi.retrofitService.setDefaultWalletAsync(userId, accName)
            setProgressBarVisibility(false)
            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("default_wallet", "result: $result")
                showSnackBar(activity.getString(R.string.wallet_set_as_default_wallet))
            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    private fun createNewWalletAsync(userId: String, navController: NavController){
        val user = getUserData().value ?: UserDataLocalModel()
        val map = hashMapOf(
            "userId" to userId
        )
        uiScope.launch {
            val propertiesDifferConfig = TempWalletApi.retrofitService.createNewWalletAsync(map)
            setProgressBarVisibility(false)
            try {
                val result = propertiesDifferConfig.await()

                Log.d("create_pin", "Create wallet result = $result")

                if(result.get("status").asBoolean){
                    navController.navigate(R.id.action_mangeWalletFragment_to_createWalletSuccessFragment)
                    getAllUsersWalletsAsync(user.id)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_pages", "error", e)
                setProgressBarVisibility(false)

            }
        }
    }
    
    private fun getAllUsersWalletsAsync(userId: Int){
        setProgressBarVisibility(true)
        //clear list wallet
        list_temp_wallets_.clear()
        uiScope.launch {
            val propertiesDifferConfig = TempWalletApi.retrofitService.getAllUserWalletsAsync(userId)
            setProgressBarVisibility(false)
            try {
                val result = propertiesDifferConfig.await()
                Log.d("user_wallet", "wallet = $result")
                val data = result.get("data").asJsonArray

                for(i in 0 until data.size()){
                    val wallet = data[i].asJsonObject.getTempWallet()
                    list_temp_wallets_.add(wallet)
                }

                setListTempWallets(list_temp_wallets_)
                setProgressBarVisibility(false)
            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }
    fun verifyUserPinAsync(userPin: String, token: String, action: String){
     //   val user = getUserData().value ?: UserDataLocalModel()
        setProgressBarVisibility(true)
        uiScope.launch {

            //navigate to success on click on next button


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
//                                //deleteBankAccount(user.token)
//
//                                //
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
        }
    }


    //Show sanckbar
    fun showSnackBar(message: String){
        val view = (activity as ManageWalletActivity).findViewById<View>(R.id.parent_layout)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }

}