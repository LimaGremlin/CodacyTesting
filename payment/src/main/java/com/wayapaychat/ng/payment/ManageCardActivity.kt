package com.wayapaychat.ng.payment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.ng.payment.databinding.ActivityManageCardBinding
import com.wayapaychat.ng.payment.databinding.ActivityManageWalletBinding
import com.wayapaychat.ng.payment.managecard.CardsAdapter
import com.wayapaychat.ng.payment.managecard.CardsViewModel
import com.wayapaychat.ng.payment.managecard.ManageCardViewModel
import com.wayapaychat.ng.payment.managewallet.ManageWalletViewModel

class ManageCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageCardBinding
    private lateinit var viewModel: ManageCardViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_card)
        navController = this.findNavController(R.id.nav_host_fragment)

        val viewModelFactory = PaymentViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ManageCardViewModel::class.java)

        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key),
            Context.MODE_PRIVATE)
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)

        viewModel.getUserRoom()

        viewModel.getUserData().observe(this, Observer {
            viewModel.getListUsersCard(it.token)
        })

        viewModel.getAllUsersWalletsAsync(id)

        binding.addCard.setOnClickListener {
            navController.navigate(R.id.action_manageCardHomeFragment_to_chooseWalletFragment2)
        }



        viewModel.setProgressBarVisibility(true)

        binding.viewModel = viewModel
    }
}