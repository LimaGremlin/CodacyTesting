package com.wayapay.ng.landingpage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.databinding.ActivityEventsBinding
import com.wayapay.ng.landingpage.events.EventViewModel
import com.wayapay.ng.landingpage.models.*
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.servicesutils.WayaDonation


class EventsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventsBinding
    private lateinit var viewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_events)
        setSupportActionBar(binding.toolbar)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EventViewModel::class.java)

        val bundle = intent.extras
        if(bundle != null){
            val event = bundle.getSerializable(IntentBundles.EVENT_KEY) as WayaEvent
            viewModel.setWayaEvent(event)
        }

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.optionsButton.setOnClickListener {
            openOptionsMenu()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RequestCodes.EVENT_EDIT_ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val event = data?.extras?.getSerializable(IntentBundles.EVENT_KEY) as WayaEvent
                    viewModel.setWayaEvent(event)
                    viewModel.showSnackBar(getString(R.string.event_updated_successfully))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.events_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val event = viewModel.getWayaEvent().value ?: WayaEvent()

        if(event.isOwner){
            menu.findItem(R.id.report_event_menu).isVisible = false
        }else
            menu.findItem(R.id.edit_menu).isVisible = false

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.make_reservation_menu -> {
                true
            }
            R.id.share_menu -> {
                true
            }
            R.id.edit_menu -> {
                val event = viewModel.getWayaEvent().value ?: WayaEvent()
                event.tag = Tags.EDIT
                val intent = Intent(this, NewEventActivity::class.java)
                intent.putExtra(IntentBundles.EVENT_KEY, event)
                startActivityForResult(intent, RequestCodes.EVENT_EDIT_ACTIVITY_RESULT)
                true
            }
            R.id.report_event_menu -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}