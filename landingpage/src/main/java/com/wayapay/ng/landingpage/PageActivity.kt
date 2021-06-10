package com.wayapay.ng.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.databinding.ActivityPageBinding
import com.wayapay.ng.landingpage.models.*
import com.wayapay.ng.landingpage.pages.PagesViewModel
import com.wayapaychat.local.room.models.WayaGramUser

class PageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPageBinding
    private lateinit var viewModel: PagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_page)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PagesViewModel::class.java)
        setSupportActionBar(binding.toolbar)

        //check if intent came with a bundle
        val bundle = intent.extras
        if(bundle != null){
            val page = bundle.getSerializable(IntentBundles.PAGES_KEY) as WayaPages
            viewModel.setPages(page)
            val user = bundle.getSerializable(IntentBundles.GRAM_PROFILE_KEY) as WayaGramUser
            viewModel.setUser(user)
        }

        binding.optionsButton.setOnClickListener {
            this.openOptionsMenu()
        }

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.page_visitor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.page_mute_menu -> {
                true
            }
            R.id.page_invite_menu -> {
                true
            }
            R.id.page_report_menu -> {
                true
            }
            R.id.page_exist_menu -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}