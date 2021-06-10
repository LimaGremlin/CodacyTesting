package com.wayapay.ng.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.databinding.ActivityNewGroupBinding
import com.wayapay.ng.landingpage.group.GroupViewModel
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

class NewGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewGroupBinding
    private lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_group)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GroupViewModel::class.java)

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}