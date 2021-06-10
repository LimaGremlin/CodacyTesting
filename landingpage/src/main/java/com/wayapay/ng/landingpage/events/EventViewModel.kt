package com.wayapay.ng.landingpage.events

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.material.snackbar.Snackbar
import com.wayapay.ng.landingpage.EventsActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.SocialActivity
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.WayaEvent
import com.wayapay.ng.landingpage.utils.*
import com.wayapaychat.remote.services.wayagram.EventApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EventViewModel(private val activity: Activity, application: Application): AndroidViewModel(application){

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var wayaEvent: MutableLiveData<WayaEvent> = MutableLiveData()
    fun getWayaEvent():MutableLiveData<WayaEvent> = wayaEvent
    fun setWayaEvent(event: WayaEvent){wayaEvent.value = event}

    private var buttonText:MutableLiveData<String> = MutableLiveData(activity.getString(R.string.continue_))
    fun getButtonText():MutableLiveData<String> = buttonText
    fun setButtonText(string: String){buttonText.value = string}

    private var headerText:MutableLiveData<String> = MutableLiveData(activity.getString(R.string.event))
    fun getHeaderText():MutableLiveData<String> = headerText
    fun setHeaderText(string: String){headerText.value = string}

    private var isVirtualEvent:MutableLiveData<Boolean> = MutableLiveData(false)
    fun getIsVirtualEvent():MutableLiveData<Boolean> = isVirtualEvent
    fun setIsVirtualEvent(boolean: Boolean){isVirtualEvent.value = boolean}

    private val eventStartTime:MutableLiveData<Long> = MutableLiveData()
    fun getEventStartTime():MutableLiveData<Long> = eventStartTime
    fun setEventStartTime(time:Long){eventStartTime.value = time}

    private val eventEndTime: MutableLiveData<Long> = MutableLiveData()
    fun getEventEndTime():MutableLiveData<Long> = eventEndTime
    fun setEventEndTime(time: Long){eventEndTime.value = time}
    
    var locationHint = Transformations.map(isVirtualEvent){isVirtualEvent ->
        if(isVirtualEvent) activity.getString(R.string.event_location)
        else activity.getString(R.string.search_location)
    }
    
    var registeredCount = Transformations.map(wayaEvent){event ->
        "${activity.getString(R.string.registered_)}${event.registeredCount}"
    }
    
    var startTimeString = Transformations.map(eventStartTime){
        "${getShortDate(it)} ${activity.getString(R.string.at)} ${getTime(it)}"
    }
    
    var freeToAttend = Transformations.map(wayaEvent){
        if(it.freeToAttend) activity.getString(R.string.free_to_attend)
        else "${activity.getString(R.string.fee_)} ${it.fee}"
    }

    fun updateEvent(event: WayaEvent) {
        var body: MultipartBody.Part? = null

        if(!TextUtils.isEmpty(event.filePath)){
            body = getMultiPartBody("eventPoster", event.filePath)
        }

        val requestBody = event.toEditRequestBody()
        val map = hashMapOf(
            "eventId" to event.id,
            "organizerId" to event.creatorId,
            "location" to event.eventLocation,
            "eventName" to event.title,
            "details" to event.organizer,
            "eventStart" to event.startTime.toString(),
            "eventEnd" to event.endTime.toString(),
        )

        uiScope.launch {
            val propertiesDifferConfig =
                if(body != null) EventApi.retrofitService.updateEVentAsync(requestBody, body)
                else EventApi.retrofitService.updateEVentAsync(map)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                Log.d("update_event", "Result = $result")

                if(result.get("status").asBoolean){
                    //Convert Data to WayaEvent
                    //val wEvent = data.getWayaEvent()
                    //Add data to list of events
                    val returnIntent = Intent()
                    returnIntent.putExtra(IntentBundles.EVENT_KEY, event)
                    activity.setResult(Activity.RESULT_OK, returnIntent)
                    activity.finish()
                }

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun showSnackBar(message: String){
        /**
         * NOTE:: Only call show snack bar from EventsActivity
         * Do not call from EditEvent
         */
        val view = (activity as EventsActivity).findViewById<View>(R.id.parent_layout)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}