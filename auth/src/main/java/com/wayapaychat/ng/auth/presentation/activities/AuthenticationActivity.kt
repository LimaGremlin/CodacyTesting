package com.wayapaychat.ng.auth.presentation.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.wayapaychat.core.base.BaseActivity
import com.wayapaychat.core.contracts.InterModuleIntent
import com.wayapaychat.core.models.SelectedPlace
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.ActivityAuthenticationBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity :
    BaseActivity<ActivityAuthenticationBinding, AuthenticationActivityViewModel>() {

    private lateinit var bReceiver: BroadcastReceiver

    private lateinit var binding: ActivityAuthenticationBinding

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_authentication

    override fun getBindingVariable(): Int = 0

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getBinding(binding: ActivityAuthenticationBinding) {
        this.binding = binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getSentIntent()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        authenticationActivityViewModel.getProgressBarVisibility().observe(this, Observer {
            if(it)binding.progressLayoutVisibility.visibility = View.VISIBLE
            else binding.progressLayoutVisibility.visibility = View.GONE
        })

        /**
         * Broadcast receiver to receive addresses gotten from "FetchAddressIntentService"
         */
        bReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                //put here whaterver you want your activity to do with the intent received
                val success = intent.getBooleanExtra(InterModuleIntent.SUCCESS, false)
                if(success) {
                    val selectedPlace = intent.getSerializableExtra(InterModuleIntent.RESULT) as SelectedPlace
                    authenticationActivityViewModel.setSelectedPlace(selectedPlace)
                }
            }
        }
    }

    private fun getSentIntent() {
        intent.extras?.let { bundle ->
            when (bundle.getString("AUTH_DIRECTION")) {
                "GET_STARTED_KEY" -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.signUpFragment)
                }
                "LOGIN_KEY" -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.loginnFragment)
                }
            }
        }
    }

    override fun onResume(){
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, IntentFilter("FetchAddressIntentService"))
        //if list of feeds is empty, restart process of query list
        //if(viewModel.getListFeeds().value.isNullOrEmpty()) getLastKnownLocation()
    }

    override fun onPause (){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver)
        super.onPause()
    }
}
