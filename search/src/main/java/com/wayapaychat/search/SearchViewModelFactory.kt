package com.wayapaychat.search

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.search.main.SearchViewModel

class SearchViewModelFactory(private val activity: Activity, private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(activity, application) as T
            else -> throw IllegalArgumentException("Unknown class")
        }
    }
}