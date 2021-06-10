package com.wayapaychat.ng.payment.managebank

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.wayapaychat.core.newtwork.WayaPayApi
import com.wayapaychat.core.payment.AllBanks
import com.wayapaychat.core.payment.WayaAccounts
import com.wayapaychat.core.payment.toAllBanks
import com.wayapaychat.core.payment.toWayaAccount
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.ng.payment.R
import com.wayapaychat.remote.services.payment.AccountApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManageBankViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    private val sqLiteDB = WayaDatabase.invoke(activity.applicationContext).getUserDataDao()

    private var list_all_banks_ = mutableListOf<AllBanks>()
    private var list_users_accounts = mutableListOf<WayaAccounts>()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var listAllBanks: MutableLiveData<List<AllBanks>> = MutableLiveData()
    fun getListAllBanks():MutableLiveData<List<AllBanks>> = listAllBanks
    private fun setListAllBanks(list: List<AllBanks>){listAllBanks.value = list}
    fun getAllBanks(position: Int): AllBanks = list_all_banks_[position]

    private var listWayaAccounts: MutableLiveData<List<WayaAccounts>> = MutableLiveData()
    fun getListWayaAccounts(): MutableLiveData<List<WayaAccounts>> = listWayaAccounts
    fun setListWayaAccounts(list: List<WayaAccounts>){listWayaAccounts.value = list}

    private var wayaAccounts: MutableLiveData<WayaAccounts> = MutableLiveData()
    fun setWayaAccounts(accounts: WayaAccounts){wayaAccounts.value = accounts}
    fun getWayaAccounts():MutableLiveData<WayaAccounts> = wayaAccounts

    private var textHeader: MutableLiveData<String> = MutableLiveData()
    fun getTextHeader():MutableLiveData<String> = textHeader
    fun setTextHeader(text: String){textHeader.value = text}

    private var addOptionVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun setAddOptionVisibility(boolean: Boolean){addOptionVisibility.value = boolean}
    fun getAddOptionVisibility():MutableLiveData<Boolean> = addOptionVisibility

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
        getAllBanksAsync(user.token)
        getUserBanksAccounts(user.token)
    }

    private fun getAllBanksAsync(token: String){
        list_all_banks_.clear()
        list_all_banks_.add(AllBanks(activity.getString(R.string.select_bank), "0"))
        uiScope.launch {
            val propertiesDifferConfig = AccountApi.retrofitService.getAllBanksAsync(token)
            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.toAllBanks()
                    list_all_banks_.add(page)
                }
                setListAllBanks(list_all_banks_)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    private fun getUserBanksAccounts(token: String){
        setProgressBarVisibility(true)
        list_users_accounts.clear()
        uiScope.launch {
            val propertiesDifferConfig = AccountApi.retrofitService.getAllUserBanksAsync(token)
            try {
                val result = propertiesDifferConfig.await()
                Log.d("account_service", "result = $result")
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.toWayaAccount()
                    list_users_accounts.add(page)
                }
                setListWayaAccounts(list_users_accounts)
                setProgressBarVisibility(false)
            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    fun addBankAccount(map: HashMap<String, String>, token: String, navController: NavController){
        setProgressBarVisibility(true)
        val user = getUserData().value ?: UserDataLocalModel()
        uiScope.launch {
            val propertiesDifferConfig = AccountApi.retrofitService.addBankAccountAsync(map, token)
            try {
                val result = propertiesDifferConfig.await()
                getUserBanksAccounts(user.token)
                setProgressBarVisibility(false)
                navController.navigate(R.id.action_addBankAccountFragment_to_bankSuccessFragment)

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
            val propertiesDifferConfig = WayaPayApi.retrofitService.validatePinAsync(userPin, token)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                Log.d("create_pin", "Verify pin result = $result")

                when(result.code()){
                    200, 201 -> {
                        when(action) {
                            activity.getString(R.string.delete_bank_account)-> {
                                //create new wallet for user on success
                                deleteBankAccount(user.token)
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

    private fun deleteBankAccount(token: String){
        val account = getWayaAccounts().value ?: WayaAccounts()
        uiScope.launch {
            val propertiesDifferConfig = AccountApi.retrofitService.deleteBankAccountAsync(account.accountNumber, token)

            try {
                val result = propertiesDifferConfig.await()
                Log.d("delete_account", "deleted account = $result")
                getUserBanksAccounts(token)
                activity.onBackPressed()

            } catch (e: Exception) {
                setProgressBarVisibility(false)
                Log.d("delete_account", "error", e)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}