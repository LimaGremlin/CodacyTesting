package com.wayapay.ng.landingpage.profile

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
import com.wayapay.ng.landingpage.utils.getWayaGroup
import com.wayapay.ng.landingpage.utils.getWayaPage
import com.wayapay.ng.landingpage.utils.toWayaPost
import com.wayapaychat.core.newtwork.getWayaGramUser
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

class ProfileViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    private val wayaGramSqLiteDB = WayaDatabase.invoke(activity.applicationContext).getWayaGramDao()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var list_user_posts = mutableListOf<WayaPost>()
    private var list_pages_ = mutableListOf<WayaPages>()
    private var list_my_groups = mutableListOf<WayaGroup>()

    private var listPages: MutableLiveData<List<WayaPages>> = MutableLiveData()
    fun getListPages():MutableLiveData<List<WayaPages>> = listPages
    fun setListPages(list: List<WayaPages>){listPages.value = list}

    private var myGroupList: MutableLiveData<List<WayaGroup>> = MutableLiveData()
    fun getMyGroupList():MutableLiveData<List<WayaGroup>> = myGroupList
    fun setMyGroupList(list: List<WayaGroup>){myGroupList.value = list}

    private var listUserPost: MutableLiveData<List<WayaPost>> = MutableLiveData()
    fun setListUserPosts(posts: List<WayaPost>){listUserPost.value = posts}
    fun getListUsersPosts():MutableLiveData<List<WayaPost>> = listUserPost

    private var wayaGramUser: MutableLiveData<WayaGramUser> = MutableLiveData()
    private fun setWayaGramUser(value: WayaGramUser) {wayaGramUser.value = value }
    fun getWayaGramUser(): MutableLiveData<WayaGramUser> = wayaGramUser

    private var ownerProfile: MutableLiveData<WayaGramUser> = MutableLiveData()
    fun setOwnerProfile(value: WayaGramUser){ownerProfile.value = value}
    fun getOwnerProfile():MutableLiveData<WayaGramUser> = ownerProfile

    private var userProfileId: MutableLiveData<String> = MutableLiveData()
    fun getUserProfileId() : MutableLiveData<String> = userProfileId
    fun setUserProfileId(string: String){userProfileId.value = string}

    private var isOwner: MutableLiveData<Boolean> = MutableLiveData()
    fun getIsOwner():MutableLiveData<Boolean> = isOwner
    fun setIsOwner(boolean: Boolean){isOwner.value = boolean}

    fun getGramUserAsync(id: String) {
        uiScope.launch {
            val user = getGramUserFromDB(id) ?: WayaGramUser()
            setWayaGramUser(user)
        }
    }

    private suspend fun getGramUserFromDB(id: String): WayaGramUser?{
        return wayaGramSqLiteDB.getUserByAuthId(id)
    }

    //Save WayaGram user In DB
    private suspend fun saveUserInDB(user: WayaGramUser){
        wayaGramSqLiteDB.insert(user)
    }

    /**
     * Call this only on resume
     * This will update user profile in RoomDB after user has updated his profile
     */
    fun getUserProfileById(userId: Int){
        val owner = getIsOwner().value ?: false
        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.getProfileByUIAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")
                /**
                 * NOTE!! All stings values gotten from server comes with double quote.
                 * You are to remove those quiotes
                 */
                val user = data.getWayaGramUser()
                saveUserInDB(user)
                //if it's owner, update user profile
                if(owner)setOwnerProfile(user)

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun getOwnerProfileByID(userId: Int){
        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.getProfileByUIAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")
                /**
                 * NOTE!! All stings values gotten from server comes with double quote.
                 * You are to remove those quiotes
                 */
                val user = data.getWayaGramUser()
                setOwnerProfile(user)
                Log.d("owner_profile", "Owner profile = $user")

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun getUserPost(userId: String){
        uiScope.launch {
            val propertiesDifferConfig = PostApi.retrofitService.queryUserPostAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("search_post", data.size().toString())
                for(i in 0 until data.size()){
                    try {
                        val post = data[i].asJsonObject.toWayaPost()
                        Log.d("user_posts", "Post = $post")
                        list_user_posts.add(post)
                    }catch (nP: NullPointerException){
                        Log.d("user_posts", "error", nP)
                    }
                }

                setListUserPosts(list_user_posts)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("user_posts", "error", e)

            }
        }
    }

    fun getUserPages(userId: String){
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.getUserPagesAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getWayaPage()
                    list_pages_.add(page)
                }
                setListPages(list_pages_)

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
        list_user_posts.forEachIndexed { index, wayaPost ->
            if(wayaPost.id == post.id){
                list_user_posts.removeAt(index)
                list_user_posts.add(index, post)
                setListUserPosts(list_user_posts)
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