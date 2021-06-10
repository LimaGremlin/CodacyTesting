package com.wayapay.ng.landingpage.groupandpages

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapay.ng.landingpage.models.WayaGroup
import com.wayapay.ng.landingpage.models.WayaPages
import com.wayapay.ng.landingpage.utils.getWayaGroup
import com.wayapay.ng.landingpage.utils.getWayaPage
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.services.wayagram.GroupApi
import com.wayapaychat.remote.services.wayagram.PagesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GroupPagesViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    private val sqLiteDB = WayaDatabase.invoke(activity.applicationContext).getUserDataDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var list_my_pages_ = mutableListOf<WayaPages>()
    private var list_followed_pages_ = mutableListOf<WayaPages>()
    private var list_my_groups = mutableListOf<WayaGroup>()
    private var list_joined_groups = mutableListOf<WayaGroup>()

    private var user: MutableLiveData<WayaGramUser> = MutableLiveData()
    fun getUser():MutableLiveData<WayaGramUser> = user
    fun setUser(u: WayaGramUser){user.value = u}

    private var myGroupList: MutableLiveData<List<WayaGroup>> = MutableLiveData()
    fun getMyGroupList():MutableLiveData<List<WayaGroup>> = myGroupList
    fun setMyGroupList(list: List<WayaGroup>){myGroupList.value = list}

    private var myJoinedGroupList: MutableLiveData<List<WayaGroup>> = MutableLiveData()
    fun getMyJoinedGroupList():MutableLiveData<List<WayaGroup>> = myJoinedGroupList
    fun setMyJoinedGroupList(list: List<WayaGroup>){myJoinedGroupList.value = list}

    private var myFollowedPages: MutableLiveData<List<WayaPages>> = MutableLiveData()
    fun getMyFollowedPages():MutableLiveData<List<WayaPages>> = myFollowedPages
    fun setMyFollowedPages(list: List<WayaPages>){myFollowedPages.value = list}

    private var myPagesList: MutableLiveData<List<WayaPages>> = MutableLiveData()
    fun getMyPageList():MutableLiveData<List<WayaPages>> = myPagesList
    fun setMyPageList(list: List<WayaPages>){myPagesList.value = list}

    fun getUserPages(userId: String){
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.getUserPagesAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getWayaPage()
                    list_my_pages_.add(page)
                }
                setMyPageList(list_my_pages_)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    fun getAllUserPages(userId: String, pNo: Int = 1){
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.getFollowingPagesAsync(userId, pNo)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getWayaPage()
                    list_followed_pages_.add(page)
                }
                setMyFollowedPages(list_my_pages_)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    fun getUserGroups(userId: String){
        uiScope.launch {
            val propertiesDifferConfig = GroupApi.retrofitService.getUserGroupAsync(1, userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    Log.d("waya_pages", "data = $data")
                    val group = data[i].asJsonObject.getWayaGroup()
                    list_my_groups.add(group)
                }
                setMyGroupList(list_my_groups)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }

    fun getJoinedGroups(userId: String, pNo: Int = 1){
        uiScope.launch {
            val propertiesDifferConfig = GroupApi.retrofitService.getJoinedGroupsAsync(pNo, userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val group = data[i].asJsonObject.getWayaGroup()
                    list_joined_groups.add(group)
                }
                setMyJoinedGroupList(list_my_groups)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_pages", "error", e)

            }
        }
    }
}