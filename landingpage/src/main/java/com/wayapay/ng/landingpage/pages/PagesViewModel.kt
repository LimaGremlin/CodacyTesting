package com.wayapay.ng.landingpage.pages

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.models.WayaPages
import com.wayapay.ng.landingpage.utils.getWayaGramUser
import com.wayapaychat.core.utils.removeQuotes
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.services.wayagram.PagesApi
import com.wayapaychat.remote.services.wayagram.PagesCategories
import kotlinx.coroutines.*

class PagesViewModel(private val activity: Activity, application: Application): AndroidViewModel(application){
    
    private var list_pages_categoreis__ = mutableListOf<PagesCategories>()

    init {
        list_pages_categoreis__.add(PagesCategories("", activity.getString(R.string.select_page_category)))
    }

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var buttonText: MutableLiveData<String> = MutableLiveData(activity.getString(R.string.continue_))
    fun getButtonText():MutableLiveData<String> = buttonText
    fun setButtonText(text:String){buttonText.value = text}

    private var pages: MutableLiveData<WayaPages> = MutableLiveData()
    fun getPages():MutableLiveData<WayaPages> = pages
    fun setPages(p: WayaPages){pages.value = p}
    
    private var listPagesCategories:MutableLiveData<List<PagesCategories>> = MutableLiveData()
    fun getListPageCategories():MutableLiveData<List<PagesCategories>> = listPagesCategories
    fun setListPagesCategories(list: List<PagesCategories>){listPagesCategories.value = list}
    fun getCategory(position: Int): PagesCategories = list_pages_categoreis__[position]

    private val user: MutableLiveData<WayaGramUser> = MutableLiveData()
    fun getUser():MutableLiveData<WayaGramUser> = user
    fun setUser(u:WayaGramUser){user.value = u}
    
    fun getListPagesCategoryAsync(pageNumer: Int = 1){
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.getAllPagesCategoryAsync(pageNumer)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")

                Log.d("pages_async", "Data: $data")

                for(i in 0 until data.size()){
                    val jObject = data[i].asJsonObject
                    val category = PagesCategories(
                        id = jObject.get("id").toString().removeQuotes(),
                        name = jObject.get("name").toString().removeQuotes()
                    )
                    list_pages_categoreis__.add(category)
                }
                setListPagesCategories(list_pages_categoreis__)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)
        setListPagesCategories(list_pages_categoreis__)

            }
        }
    }

    fun getPageFollowers(){
        val user = getUser().value ?: WayaGramUser()
        val page = getPages().value ?: WayaPages()
        uiScope.launch {
            val propertiesDifferConfig = PagesApi.retrofitService.getAllPageFollowersAsync(user.id, page.pageId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonArray("data")
                Log.d("get_page_users", "Data: $data")
                for(i in 0 until data.size()){
                    val jObject = data[i].asJsonObject.getWayaGramUser()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

}