package com.wayapay.ng.landingpage.models

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.chat.ChatViewModel
import com.wayapay.ng.landingpage.donation.DonationViewModel
import com.wayapay.ng.landingpage.editprofile.EditGramProfileViewModel
import com.wayapay.ng.landingpage.events.EventViewModel
import com.wayapay.ng.landingpage.group.GroupViewModel
import com.wayapay.ng.landingpage.groupandpages.GroupPagesViewModel
import com.wayapay.ng.landingpage.main.LandingViewModel
import com.wayapay.ng.landingpage.pages.PagesViewModel
import com.wayapay.ng.landingpage.payment.PayViewModel
import com.wayapay.ng.landingpage.post.PostViewModel
import com.wayapay.ng.landingpage.profile.ProfileViewModel
import com.wayapay.ng.landingpage.search.SearchViewModel

class LandingViewModelFactory (private val activity: Activity,
                               private val application: Application): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LandingViewModel::class.java) ->
                LandingViewModel(activity, application) as T
            modelClass.isAssignableFrom(EventViewModel::class.java) ->
                EventViewModel(activity, application) as T
            modelClass.isAssignableFrom(PostViewModel::class.java) ->
                PostViewModel(activity, application) as T
            modelClass.isAssignableFrom(PagesViewModel::class.java) ->
                PagesViewModel(activity, application) as T
            modelClass.isAssignableFrom(GroupViewModel::class.java) ->
                GroupViewModel(activity, application) as T
            modelClass.isAssignableFrom(GroupPagesViewModel::class.java) ->
                GroupPagesViewModel(activity, application) as T
            modelClass.isAssignableFrom(DonationViewModel::class.java) ->
                DonationViewModel(activity, application) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                ProfileViewModel(activity, application) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(activity, application) as T
            modelClass.isAssignableFrom(ChatViewModel::class.java) ->
                ChatViewModel(activity, application) as T
            modelClass.isAssignableFrom(PayViewModel::class.java) ->
                PayViewModel(activity, application) as T
            modelClass.isAssignableFrom(EditGramProfileViewModel::class.java) ->
                EditGramProfileViewModel(activity, application) as T
            else -> throw IllegalArgumentException("Unknown class")
        }
    }

}