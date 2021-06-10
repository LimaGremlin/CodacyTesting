package com.wayapaychat.ng.payment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wayapaychat.ng.payment.databinding.ActivityManageBankBinding
import com.wayapaychat.ng.payment.managebank.ManageBankViewModel

class ManageBankActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageBankBinding
    private lateinit var viewModel: ManageBankViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_bank)
        navController = this.findNavController(R.id.nav_host_fragment)

        val viewModelFactory = PaymentViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ManageBankViewModel::class.java)
        binding.viewModel = viewModel

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)
        viewModel.getUserRoom()

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.addButton.setOnClickListener {
            navigateToAddBank()
        }

        binding.lifecycleOwner = this
    }

    private fun navigateToAddBank(){
        if(navController.currentDestination?.id == R.id.manageBankHomeFragment){
            navController.navigate(R.id.action_manageBankHomeFragment_to_addBankAccountFragment)
        }
    }
}