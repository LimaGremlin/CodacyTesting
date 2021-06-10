package com.wayapay.ng.landingpage.post

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.models.WayaPost

class PostViewModel(private val activity: Activity, application: Application):AndroidViewModel(application) {
    
    private var list_choice = mutableListOf<String>()

    private var listImageUrl: MutableLiveData<MutableList<String>> = MutableLiveData()
    fun getListImageUrl():MutableLiveData<MutableList<String>> = listImageUrl
    fun setListImageUrl(list: MutableList<String>){listImageUrl.value = list}

    private var listAbsolutePath: MutableLiveData<MutableList<String>> = MutableLiveData()
    fun getListAbsolutePath():MutableLiveData<MutableList<String>> = listAbsolutePath
    fun setListAbsolutePath(list: MutableList<String>){listAbsolutePath.value = list}

    private var post: MutableLiveData<WayaPost> = MutableLiveData()
    fun setPost(wayaPost: WayaPost){post.value = wayaPost}
    fun getPost():MutableLiveData<WayaPost> = post

    private var isVotePaid: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getIsVotePaid():MutableLiveData<Boolean> = isVotePaid
    fun setIsVotePaid(boolean: Boolean){isVotePaid.value = boolean}

    private var headerText: MutableLiveData<String> = MutableLiveData(activity.getString(R.string.post))
    fun getHeaderText():MutableLiveData<String> = headerText
    fun setHeaderText(string: String){headerText.value = string}

    private var buttonText: MutableLiveData<String> = MutableLiveData(activity.getString(R.string.send))
    fun getButtonText():MutableLiveData<String> = buttonText
    fun setButtonText(string: String){buttonText.value = string}

    private var editTextHit: MutableLiveData<String> = MutableLiveData(activity.getString(R.string.create_new_post))
    fun getEditTextHint():MutableLiveData<String> = editTextHit
    fun setEditTextHint(string: String){editTextHit.value = string}
}