package com.wayapay.ng.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.databinding.ActivityNewPageBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.pages.PagesViewModel

class NewPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPageBinding
    private lateinit var viewModel: PagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_page)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PagesViewModel::class.java)

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        //Get list of all pages categories from server
        viewModel.getListPagesCategoryAsync()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}