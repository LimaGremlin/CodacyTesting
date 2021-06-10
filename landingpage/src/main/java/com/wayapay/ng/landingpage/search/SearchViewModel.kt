package com.wayapay.ng.landingpage.search

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.SearchActivity
import com.wayapay.ng.landingpage.models.WayaGroup
import com.wayapay.ng.landingpage.models.WayaPages
import com.wayapay.ng.landingpage.models.WayaPost
import com.wayapay.ng.landingpage.utils.getWayaGramUser
import com.wayapay.ng.landingpage.utils.getWayaGroup
import com.wayapay.ng.landingpage.utils.getWayaPage
import com.wayapay.ng.landingpage.utils.toWayaPost
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.services.wayagram.GroupApi
import com.wayapaychat.remote.services.wayagram.PagesApi
import com.wayapaychat.remote.services.wayagram.PostApi
import com.wayapaychat.remote.services.wayagram.GramProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class SearchViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    private val wayaGramSqLiteDB = WayaDatabase.invoke(activity.applicationContext).getWayaGramDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var list_searched_user_ = mutableListOf<WayaGramUser>()
    private var list_searched_pages = mutableListOf<WayaPages>()
    private var list_searched_groups = mutableListOf<WayaGroup>()
    private var list_searched_posts = mutableListOf<WayaPost>()

    private var userId: MutableLiveData<String> = MutableLiveData()
    fun setUserId(string: String){userId.value = string}
    fun getUserId():MutableLiveData<String> = userId

    private var wayaGramUser: MutableLiveData<WayaGramUser> = MutableLiveData()
    private fun setWayaGramUser(value: WayaGramUser) {wayaGramUser.value = value }
    fun getWayaGramUser(): MutableLiveData<WayaGramUser> = wayaGramUser

    private var listSearchedUsers: MutableLiveData<List<WayaGramUser>> = MutableLiveData()
    fun getListSearchedUsers():MutableLiveData<List<WayaGramUser>> = listSearchedUsers
    fun setListSearchedUsers(list: List<WayaGramUser>){listSearchedUsers.value = list}

    private var listSearchedPages: MutableLiveData<List<WayaPages>> = MutableLiveData()
    fun setListSearchedPages(list: List<WayaPages>){listSearchedPages.value = list}
    fun getListSearchedPages():MutableLiveData<List<WayaPages>> = listSearchedPages

    private var listSearchedGroups: MutableLiveData<List<WayaGroup>> = MutableLiveData()
    fun getListSearchedGroups():MutableLiveData<List<WayaGroup>> = listSearchedGroups
    fun setListSearchedGroups(list: List<WayaGroup>){listSearchedGroups.value = list}

    private var listSearchedPost: MutableLiveData<List<WayaPost>> = MutableLiveData()
    fun setListSearchedPost(list: List<WayaPost>){listSearchedPost.value = list}
    fun getListSearchedPost():MutableLiveData<List<WayaPost>> = listSearchedPost

    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun setProgressBarVisibility(boolean: Boolean){progressBarVisibility.value = boolean}
    fun getProgressBarVisibility():MutableLiveData<Boolean> = progressBarVisibility

    fun getGramUserAsync(id: String) {
        uiScope.launch {
            val user = getGramUserFromDB(id) ?: WayaGramUser()
            setWayaGramUser(user)
        }
    }

    private suspend fun getGramUserFromDB(id: String): WayaGramUser?{
        return wayaGramSqLiteDB.getUserByAuthId(id)
    }

    fun searchUsers(param: String, uid:String){
        setProgressBarVisibility(true)
        list_searched_user_.clear()
        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.queryUserAsync(param, uid)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                setProgressBarVisibility(false)
                for(i in 0 until data.size()){
                    val userData = data[i].asJsonObject.getWayaGramUser()
                    //Add data to list of events
                    list_searched_user_.add(userData)
                    //Set list of events
                }
                setListSearchedUsers(list_searched_user_)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun searchPages(param: String, number: Int = 1, userId: String){
        setProgressBarVisibility(true)
        list_searched_pages.clear()
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.queryPagesAsync(param, number, userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                setProgressBarVisibility(false)
                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getWayaPage()
                    //Add data to list of events
                    list_searched_pages.add(page)
                    //Set list of events
                }
                setListSearchedPages(list_searched_pages)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun searchGroups(userId: String, param: String, number: Int = 1){
        setProgressBarVisibility(true)
        list_searched_groups.clear()
        uiScope.launch {
            val propertiesDifferConfig = GroupApi.retrofitService.queryGroupAsync(param, number, userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                setProgressBarVisibility(false)
                for(i in 0 until data.size()){
                    val group = data[i].asJsonObject.getWayaGroup()
                    //Add data to list of events
                    list_searched_groups.add(group)
                    //Set list of events
                }
                setListSearchedGroups(list_searched_groups)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun searchPost(param: String){
        setProgressBarVisibility(true)
        list_searched_posts.clear()
        uiScope.launch {
            val propertiesDifferConfig = PostApi.retrofitService.queryPostAsync(param)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                setProgressBarVisibility(false)
                for(i in 0 until data.size()){
                    try {
                        val post = data[i].asJsonObject.toWayaPost()
                        list_searched_posts.add(post)
                    }catch (nP: NullPointerException){
                    }
                }
                setListSearchedPost(list_searched_posts)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("search_post", "error", e)

            }
        }
    }

    fun voteOnPost(map: HashMap<String, String>){
        uiScope.launch {
            val propertiesDifferConfig = PostApi.retrofitService.voteAPostAsync(map)

            try {
                val result = propertiesDifferConfig.await()
                Log.d("vote_post", "Result $result")
                val data = result.getAsJsonObject("data")
                if(result.get("status").asBoolean){
                    showSnackBar(activity.getString(R.string.your_vote_has_been_recorded))
                }

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("publish_post", "error", e)

            }
        }
    }

    fun updatePost(post: WayaPost){
        list_searched_posts.forEachIndexed { index, wayaPost ->
            if(wayaPost.id == post.id){
                list_searched_posts.removeAt(index)
                list_searched_posts.add(index, post)
                setListSearchedPost(list_searched_posts)
                return
            }
        }
    }

    //Show sanckbar
    fun showSnackBar(message: String){
        val view = (activity as SearchActivity).findViewById<View>(R.id.parent_layout)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()
        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}