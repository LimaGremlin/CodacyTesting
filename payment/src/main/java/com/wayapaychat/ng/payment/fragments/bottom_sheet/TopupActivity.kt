package com.wayapaychat.ng.payment.fragments.bottom_sheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.ActivityManageCardBinding
import com.wayapaychat.ng.payment.databinding.ActivityTopupBinding
import com.wayapaychat.ng.payment.managecard.ManageCardViewModel

class TopupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopupBinding
    private lateinit var viewModel: ManageCardViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_topup)
        navController = this.findNavController(R.id.nav_host_fragment)

        val viewModelFactory = PaymentViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ManageCardViewModel::class.java)

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}