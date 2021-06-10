package com.wayapay.ng.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wayapay.ng.landingpage.databinding.ActivityNewEventBinding
import com.wayapay.ng.landingpage.events.EventViewModel
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.WayaEvent

class NewEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewEventBinding
    private lateinit var viewModel: EventViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_event)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EventViewModel::class.java)
        navController = this.findNavController(R.id.nav_host_fragment)

        //check if intent came with a bundle
        val bundle = intent.extras
        if(bundle != null){
            val event = bundle.getSerializable(IntentBundles.EVENT_KEY) as WayaEvent
            viewModel.setWayaEvent(event)
            viewModel.setIsVirtualEvent(event.virtual)
        }

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onBackPressed() {
        viewModel.setButtonText(getString(R.string.continue_))
        super.onBackPressed()
    }
}