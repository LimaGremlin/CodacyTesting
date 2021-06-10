package com.wayapay.ng.landingpage.editprofile

import android.app.Activity
import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.wayapay.ng.landingpage.EditGramProfileActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.SearchActivity
import com.wayapay.ng.landingpage.utils.getMultiPartBody
import com.wayapaychat.core.newtwork.getWayaGramUser
import com.wayapaychat.core.utils.removeQuotes
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.services.wayagram.GramProfileApi
import com.wayapaychat.remote.services.wayagram.GroupApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditGramProfileViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {
    private val wayaGramSqLiteDB = WayaDatabase.invoke(activity.applicationContext).getWayaGramDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var wayaGramUser: MutableLiveData<WayaGramUser> = MutableLiveData()
    private fun setWayaGramUser(value: WayaGramUser) {wayaGramUser.value = value }
    fun getWayaGramUser(): MutableLiveData<WayaGramUser> = wayaGramUser

    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private fun setProgressbarVisibility(boolean: Boolean){progressBarVisibility.value = boolean}
    fun getProgressBarVisibility():MutableLiveData<Boolean> = progressBarVisibility

    private var editGramProfile: MutableLiveData<EditGramProfile> = MutableLiveData()
    fun setEditGramProfile(profile: EditGramProfile){editGramProfile.value = profile}
    fun getEditGramProfile():MutableLiveData<EditGramProfile> = editGramProfile

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

                Log.d("waya_gram", "Data = $data")

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

    fun updateUserProfile(){
        val profile = getEditGramProfile().value ?: EditGramProfile()
        setProgressbarVisibility(true)
        val user = getWayaGramUser().value ?: WayaGramUser()

        val groupAsync = hashMapOf(
            "user_id" to user.authId,
            "notPublic" to profile.isPrivate.toString(),
        )
        //Check if user want's to update username
        if(user.username != profile.username)groupAsync["username"] = profile.username

        val groupRequestBody = profile.toMapRequestBody(user.authId)
        var profileImg: MultipartBody.Part? = null
        var headerImg: MultipartBody.Part? = null

        if(!TextUtils.isEmpty(profile.profileImgPath))
            profileImg = getMultiPartBody("avatar", profile.profileImgPath)

        if(!TextUtils.isEmpty(profile.headerImgPath))
            headerImg = getMultiPartBody("coverImage", profile.headerImgPath)

        uiScope.launch {

            val propertiesDifferConfig = if(profileImg == null && headerImg == null)
                GramProfileApi.RETROFIT_SERVICE.updateProfileAsync(groupAsync)
            else if (profileImg != null && headerImg != null)
                GramProfileApi.RETROFIT_SERVICE.updateProfileAsync(groupRequestBody, profileImg, headerImg)
            else if (profileImg != null)
                GramProfileApi.RETROFIT_SERVICE.updateProfileAsync(groupRequestBody, profileImg)
            else if (headerImg != null)
                GramProfileApi.RETROFIT_SERVICE.updateProfileAsync(groupRequestBody, headerImg)
            else null

            try {
                if(propertiesDifferConfig == null) {
                    setProgressbarVisibility(false)
                    return@launch
                }
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                setProgressbarVisibility(false)

                if(result.get("status").asBoolean){
                    getWayaProfileByID(user.authId.toIntOrNull() ?: -1)
                    activity.onBackPressed()
                }else{
                    showSnackBar(result.get("message").toString().removeQuotes())
                }
            }catch (e: Exception){
                setProgressbarVisibility(false)
                e.printStackTrace()

            }
        }
    }

    //Show sanckbar
    fun showSnackBar(message: String){
        val view = (activity as EditGramProfileActivity).findViewById<View>(R.id.parent_layout)
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        //Set action
        snackbar.setAction(activity.getString(R.string.close)) {
            snackbar.dismiss() // close snack bar
        }.show()

    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}