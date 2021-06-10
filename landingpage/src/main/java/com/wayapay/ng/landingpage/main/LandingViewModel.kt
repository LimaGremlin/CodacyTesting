package com.wayapay.ng.landingpage.main

import android.app.Activity
import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.material.snackbar.Snackbar
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.SocialActivity
import com.wayapay.ng.landingpage.models.WayaEvent
import com.wayapay.ng.landingpage.models.WayaGroup
import com.wayapay.ng.landingpage.models.WayaPages
import com.wayapay.ng.landingpage.models.WayaPost
import com.wayapay.ng.landingpage.utils.*
import com.wayapaychat.core.utils.removeQuotes
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.services.wayagram.*
import com.wayapaychat.remote.servicesutils.WayaDonation
import com.wayapaychat.remote.servicesutils.toMapRequestBody
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.NullPointerException

class LandingViewModel(private val activity: Activity, application: Application) :
    AndroidViewModel(application) {

    private val sqLiteDB = WayaDatabase.invoke(activity.applicationContext).getUserDataDao()
    private val wayaGramSqLiteDB = WayaDatabase.invoke(activity.applicationContext).getWayaGramDao()

    private var userData: MutableLiveData<UserDataLocalModel> = MutableLiveData()
    fun getUserData(): MutableLiveData<UserDataLocalModel> = userData
    private fun setUserData(data: UserDataLocalModel){userData.value = data}

    private var list_all_events = mutableListOf<WayaEvent>()
    private var list_events_ = mutableListOf<WayaEvent>()
    private var list_post = mutableListOf<WayaPost>()
    private var list_pages_ = mutableListOf<WayaPages>()

    private var list_searched_user_ = mutableListOf<WayaGramUser>()
    private var list_searched_pages = mutableListOf<WayaPages>()
    private var list_searched_groups = mutableListOf<WayaGroup>()
    private var list_searched_posts = mutableListOf<WayaPost>()
    private var list_user_posts = mutableListOf<WayaPost>()

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var listUserPost: MutableLiveData<List<WayaPost>> = MutableLiveData()
    fun setListUserPosts(posts: List<WayaPost>){listUserPost.value = posts}
    fun getListUsersPosts():MutableLiveData<List<WayaPost>> = listUserPost

    private var listAllEvents: MutableLiveData<List<WayaEvent>> = MutableLiveData()
    fun setListAllEvents(events: List<WayaEvent>){listAllEvents.value = events}
    fun getListAllEvents():MutableLiveData<List<WayaEvent>> = listAllEvents

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

    private var drawerEnabled: MutableLiveData<Boolean> = MutableLiveData(true)
    fun getDrawerEnabled():MutableLiveData<Boolean> = drawerEnabled
    fun setDrawerEnabled(boolean: Boolean){drawerEnabled.value = boolean}

    private var isBottomNavVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    fun getIsBottomNavVisible():MutableLiveData<Boolean> = isBottomNavVisible
    fun setIsBottomNavVisible(boolean: Boolean){isBottomNavVisible.value = boolean}

    private var isHeaderVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    fun setIsHeaderVisible(boolean: Boolean){isHeaderVisible.value = boolean}
    fun getIsHeaderVisible():MutableLiveData<Boolean> = isHeaderVisible

    private var searchButtonVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun setSearchButtonVisibility(boolean: Boolean){searchButtonVisibility.value = boolean}
    fun getSearchButtonVisibility():MutableLiveData<Boolean> = searchButtonVisibility

    private var isBackEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getIsBackEnabled():MutableLiveData<Boolean> = isBackEnabled
    fun setIsBackEnabled(boolean: Boolean){isBackEnabled.value = boolean}

    private var fabVisibility: MutableLiveData<Boolean> = MutableLiveData(true)
    fun setFabVisibility(boolean: Boolean){fabVisibility.value = boolean}
    fun getFabVisibility():MutableLiveData<Boolean> = fabVisibility

    private var listPages: MutableLiveData<List<WayaPages>> = MutableLiveData()
    fun getListPages():MutableLiveData<List<WayaPages>> = listPages
    fun setListPages(list: List<WayaPages>){listPages.value = list}

    private var searchVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getSearchVisibility():MutableLiveData<Boolean> = searchVisibility
    fun setSearchVisibility(boolean: Boolean){searchVisibility.value = boolean}

    private var listOptions: MutableLiveData<List<String>> = MutableLiveData()
    fun getListOptions(): MutableLiveData<List<String>> = listOptions
    fun setListOptions(list: List<String>) {
        listOptions.value = list
    }

    private var optionsParentVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getOptionsParentVisibility(): MutableLiveData<Boolean> = optionsParentVisibility
    fun setOptionsParentVisibility(boolean: Boolean) {
        optionsParentVisibility.value = boolean
    }

    private var headerText: MutableLiveData<String> =
        MutableLiveData(activity.getString(R.string.wayagram))

    fun setHeaderText(string: String) {
        headerText.value = string
    }

    fun getHeaderText(): MutableLiveData<String> = headerText

    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun setProgressBarVisibility(boolean: Boolean) {
        progressBarVisibility.value = boolean
    }

    fun getProgressBarVisibility(): MutableLiveData<Boolean> = progressBarVisibility

    private var addPeopleVisibility: MutableLiveData<Boolean> = MutableLiveData(true)
    fun getAddPeopleVisibility(): MutableLiveData<Boolean> = addPeopleVisibility
    fun setAddPeopleVisibility(boolean: Boolean) {
        addPeopleVisibility.value = boolean
    }

    private var optionsVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getOptionsVisibility(): MutableLiveData<Boolean> = optionsVisibility
    fun setOptionsVisibility(boolean: Boolean) {
        optionsVisibility.value = boolean
    }

    private var wayaGramUser: MutableLiveData<WayaGramUser> = MutableLiveData()
    private fun setWayaGramUser(value: WayaGramUser) {wayaGramUser.value = value }
    fun getWayaGramUser(): MutableLiveData<WayaGramUser> = wayaGramUser

    private var selectedEvent: MutableLiveData<WayaEvent> = MutableLiveData()
    fun getSelectedEvent(): MutableLiveData<WayaEvent> = selectedEvent
    fun setSelectedEvent(event: WayaEvent) {
        selectedEvent.value = event
    }

    private var selectedPost: MutableLiveData<WayaPost> = MutableLiveData()
    fun setSelectedPost(post: WayaPost){selectedPost.value = post}
    fun getSelectedPost():MutableLiveData<WayaPost> = selectedPost

    private var listEvents: MutableLiveData<List<WayaEvent>> = MutableLiveData()
    fun getListEvents(): MutableLiveData<List<WayaEvent>> = listEvents
    fun setListEvents(events: List<WayaEvent>) {
        listEvents.value = events
        list_events_ = events.toMutableList()
    }

    private var listPost: MutableLiveData<List<WayaPost>> = MutableLiveData()
    fun getListPost():MutableLiveData<List<WayaPost>> = listPost
    fun setListPost(posts: List<WayaPost>){
        listPost.value = posts
        list_post = posts.toMutableList()
    }


    var firstAndLastName = Transformations.map(wayaGramUser) { user ->
        "${user.firstName} ${user.surname}"
    }

    //add a new item to sqlite db using room
    fun getUserRoom() {
        uiScope.launch {
            getAuthUserDataFromDB()
        }
    }
    private suspend fun getAuthUserDataFromDB() {
        val user = sqLiteDB.getUserLoginData()
        getWayaGramUserFromDB(user)
    }

    private suspend fun getWayaGramUserFromDB(authUser: UserDataLocalModel){
        val user = wayaGramSqLiteDB.getUserByAuthId(authUser.id.toString())
        Log.d("waya_gram_user", "User = $user")
        Log.d("waya_gram_user", "Token = ${authUser.token}")
        if(user != null) {
            setWayaGramUser(user)
            setUserData(authUser)
            firstQueries(user.id)
        }
        //Get user WayaGram profile from server
        getWayaProfileByID(authUser.id)
    }

    private fun firstQueries(id: String){
        getListAllEvents(1)
        getUserPages(id)
        //Search users for discover page
        searchUsers("a", id)
        searchPages("a", 1, id)
        searchGroups(id, "a", 1)
        getUserPost(id)
    }

    fun connectToChatService(token: String, userId: String){
        uiScope.launch {
            val propertiesDifferConfig = ChatApi.retrofitService.connectAsync(token, userId)

            try {
                val result = propertiesDifferConfig.await()
                Log.d("chat_service", "Data = $result")
                getListOpenChats(token)
                connectToChatService(token)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("chat_service", "error", e)

            }
        }
    }

    fun getListOpenChats(token: String){
        uiScope.launch {
            val propertiesDifferConfig = ChatApi.retrofitService.chatOverViewAsync(token)

            try {
                val result = propertiesDifferConfig.await()
                Log.d("chat_service", "Data = $result")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("chat_service", "error", e)

            }
        }
    }

    fun connectToChatService(token: String){
        uiScope.launch {
            val propertiesDifferConfig = ChatApi.retrofitService.userConnectAsync(token)

            try {
                val result = propertiesDifferConfig.await()
                Log.d("chat_service", "Data = $result")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("chat_service", "error", e)

            }
        }
    }

    fun followPageAsync(pageID: String, userId: String){
        val map = hashMapOf(
                "pageId" to pageID,
                "userId" to userId
        )
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.followUnFollowPageAsync(map)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")
                Log.d("follow_page", "Result = $result")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("follow_page", "error", e)

            }
        }
    }

    fun searchUsers(param: String, uid:String){
        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.queryUserAsync(param, uid)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("search_user", "Data = $data")
                for(i in 0 until data.size()){
                    val userData = data[i].asJsonObject.getWayaGramUser()
                    //Add data to list of events
                    list_searched_user_.add(userData)
                    //Set list of events
                    setListSearchedUsers(list_searched_user_)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun updateListSearchedPages(pages: WayaPages){
        list_searched_pages.forEachIndexed { index, page ->
            if(page.pageId == pages.pageId){
                list_searched_pages.removeAt(index)
                list_searched_pages.add(index, pages)
                return
            }
        }
    }

    fun searchPages(param: String, number: Int = 1, userId: String){
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.queryPagesAsync(param, number, userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("search_pages", "Data = $data")
                for(i in 0 until data.size()){
                    val page = data[i].asJsonObject.getWayaPage()
                    Log.d("searched_pages", "last page = ${data[i]}")
                    //Add data to list of events
                    list_searched_pages.add(page)
                    //Set list of events
                    setListSearchedPages(list_searched_pages)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun searchGroups(userId: String, param: String, number: Int = 1){
        uiScope.launch {
            val propertiesDifferConfig = GroupApi.retrofitService.queryGroupAsync(param, number, userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("search_groups", "Data = $data")
                for(i in 0 until data.size()){
                    val group = data[i].asJsonObject.getWayaGroup()
                    //Add data to list of events
                    list_searched_groups.add(group)
                    //Set list of events
                    setListSearchedGroups(list_searched_groups)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun searchPost(param: String){
        uiScope.launch {
            val propertiesDifferConfig = PostApi.retrofitService.queryPostAsync(param)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("search_post", data.size().toString())
                for(i in 0 until data.size()){
                    try {
                        val post = data[i].asJsonObject.toWayaPost()
                        Log.d("search_post", "Post = $post")
                        list_searched_posts.add(post)
                    }catch (nP: NullPointerException){
                        Log.d("search_post", "error", nP)
                    }
                }

                setListSearchedPost(list_searched_posts)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("search_post", "error", e)

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
                setListPost(list_user_posts)

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

    fun getWayaProfileByID(userId: Int){

        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.getProfileByUIAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                val gramUser = getWayaGramUser().value
                /**
                 * NOTE!! All stings values gotten from server comes with double quote.
                 * You are to remove those quiotes
                 */
                val user = data.getWayaGramUser()
                setWayaGramUser(user)
                //save User Data in Room DB
                saveUserInDB(user)
                if(gramUser == null ) firstQueries(user.id)

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    //Save WayaGram user In DB
    private suspend fun saveUserInDB(user: WayaGramUser){
        wayaGramSqLiteDB.insert(user)
    }

    fun getListAllEvents(pNO: Int = 1){
        uiScope.launch {
            val propertiesDifferConfig = EventApi.retrofitService.getAllEventsAsync(pNO)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val wayaEvent = data[i].asJsonObject.getWayaEvent()
                    //Add data to list of events
                    list_all_events.add(wayaEvent)
                    //Set list of events
                    setListAllEvents(list_all_events)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun getUserEventAsync(userId: String, pNumber: Int){

        uiScope.launch {
            val propertiesDifferConfig = EventApi.retrofitService.getUserEventAsync(userId, pNumber)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                for(i in 0 until data.size()){
                    val wayaEvent = data[i].asJsonObject.getWayaEvent()
                    //Add data to list of events
                    list_events_.add(wayaEvent)
                    //Set list of events
                    setListEvents(list_events_)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun publishDonation(donation: WayaDonation) {
        var body: MultipartBody.Part? = null

        if(!TextUtils.isEmpty(donation.filePath)){
            body = getMultiPartBody("donationImage", donation.filePath)
        }

        val map = hashMapOf(
            "profileId" to donation.ownerId,
            "title" to donation.title,
            "description" to donation.description,
            "estimatedAmount" to donation.target.toString(),
            "displayTotalDonation" to donation.displayTotalDonation.toString(),
        )
        val requestBody = donation.toMapRequestBody()

        uiScope.launch {
            val propertiesDifferConfig =
                if(body != null) DonationApi.retrofitService.createDonationAsync(requestBody, body)
                else DonationApi.retrofitService.createDonationAsync(map)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")
                Log.d("create_donation", "result: $result")
                if(result.get("status").asBoolean){
                    showSnackBar(activity.getString(R.string.your_donation_has_been_created_successfully))
                }

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun publishEvent(event: WayaEvent) {
        var body: MultipartBody.Part? = null

        if(!TextUtils.isEmpty(event.filePath)){
            body = getMultiPartBody("eventPoster", event.filePath)
        }

        val map = event.toMapRequestBody()
        val wayaAsync = event.toEventAsync()

        uiScope.launch {
            val propertiesDifferConfig =
                if(body != null) EventApi.retrofitService.createEventAsync(map, body)
                else EventApi.retrofitService.createEventAsync(wayaAsync)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                if(result.get("status").asBoolean){
                    showSnackBar(activity.getString(R.string.your_event_has_been_created))
                    //Convert Data to WayaEvent
                    val wEvent = data.getWayaEvent()
                    //Add data to list of events
                    list_events_.add(wEvent)
                    //Set list of events
                    setListEvents(list_events_)
                }

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun publishGroup(group: WayaGroup){

        val groupAsync = group.toGroupAsync()
        val groupRequestBody = group.toMapRequestBody()
        var profileImg: MultipartBody.Part? = null
        var headerImg: MultipartBody.Part? = null

        if(!TextUtils.isEmpty(group.profileImagePath))
            profileImg = getMultiPartBody("image", group.profileImagePath)

        if(!TextUtils.isEmpty(group.profileImagePath))
            headerImg = getMultiPartBody("headerImage", group.profileImagePath)

        uiScope.launch {

            val propertiesDifferConfig = if(profileImg == null && headerImg == null)
                GroupApi.retrofitService.createGroupAsync(groupAsync)
            else if (profileImg != null && headerImg != null)
                GroupApi.retrofitService.createGroupAsync(groupRequestBody, profileImg, headerImg)
            else if (profileImg != null)
                GroupApi.retrofitService.createGroupAsync(groupRequestBody, profileImg)
            else if (headerImg != null)
                GroupApi.retrofitService.createGroupAsync(groupRequestBody, headerImg)
            else null

            try {
                if(propertiesDifferConfig == null) return@launch
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                if(result.get("status").asBoolean){
                    showSnackBar("Yor group has been created")
                }
            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_page", "error", e)

            }
        }
    }

    fun publishPage(pages: WayaPages){
        
        val pageAsync = pages.toPagesAsync()
        val pageRequestBody = pages.toMapRequestBody()
        var profileImg: MultipartBody.Part? = null
        var headerImg: MultipartBody.Part? = null

        if(!TextUtils.isEmpty(pages.profileImagePath))
            profileImg = getMultiPartBody("image", pages.profileImagePath)

        if(!TextUtils.isEmpty(pages.profileImagePath))
            headerImg = getMultiPartBody("headerImage", pages.profileImagePath)

        uiScope.launch {

            val propertiesDifferConfig = if(profileImg == null && headerImg == null)
                PagesApi.retrofitService.createPageAsync(pageAsync)
            else if (profileImg != null && headerImg != null)
                PagesApi.retrofitService.createPageAsync(pageRequestBody, profileImg, headerImg)
            else if (profileImg != null)
                PagesApi.retrofitService.createPageAsync(pageRequestBody, profileImg)
            else if (headerImg != null)
                PagesApi.retrofitService.createPageAsync(pageRequestBody, headerImg)
            else null

            try {
                if(propertiesDifferConfig == null) return@launch
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                if(result.get("status").asBoolean){
                    showSnackBar(activity.getString(R.string.your_page_has_been_created))
                }
            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_page", "error", e)

            }
        }
    }

    fun updatePostAsync(map: HashMap<String, String>){

    }

    private fun uploadPostImgAsync(userId: String, postId: String, body: MultipartBody.Part){
        val profile_id = RequestBody.create(okhttp3.MultipartBody.FORM, userId)
        val post_id = RequestBody.create(okhttp3.MultipartBody.FORM, postId)
        val map = hashMapOf(
            "profile_id" to profile_id,
            "post_id" to post_id,
        )

        uiScope.launch {

            val propertiesDifferConfig = PostApi.retrofitService.updatePostAsync(map, body)
            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram_post", "error", e)

            }
        }
    }

    fun publishPost(post: WayaPost){

        uiScope.launch {
            val propertiesDifferConfig = PostApi.retrofitService.createPostAsync(post.getPostAsysnc())

            try {
                val result = propertiesDifferConfig.await()
                Log.d("publish_post", "Result $result")
                val data = result.getAsJsonObject("data")
                
                if(result.get("status").asBoolean){
                    showSnackBar(activity.getString(R.string.your_post_has_been_created))
                    post.id = data.get("id").toString().removeQuotes()

                    val iterator = post.listAbsolutePath.iterator()
                    while (iterator.hasNext()){
                        val body = getMultiPartBody("images", iterator.next())
                        if(body != null)
                            uploadPostImgAsync(post.userId ?: " ", post.id, body)
                    }

                    list_post.add(post)
                    setListPost(list_post)
                }

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("publish_post", "error", e)

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
        list_post.forEachIndexed { index, wayaPost ->
            if(wayaPost.id == post.id){
                list_post.removeAt(index)
                list_post.add(index, post)
                setListPost(list_post)
                return
            }
        }
    }

    //Show sanckbar
    fun showSnackBar(message: String){
        val view = (activity as SocialActivity).findViewById<View>(R.id.drawer_layout)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}
