package com.wayapaychat.ng.payment

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.ng.payment.databinding.ActivityManageWalletBinding
import com.wayapaychat.ng.payment.dialogs.NewWalletButtonSheetDialog
import com.wayapaychat.ng.payment.managewallet.ManageWalletViewModel

class ManageWalletActivity : AppCompatActivity(), NewWalletButtonSheetDialog.OnClickListener {
    private lateinit var binding: ActivityManageWalletBinding
    private lateinit var viewModel: ManageWalletViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_wallet)
        setSupportActionBar(binding.toolbar)
        binding.optionsButton.setOnClickListener {
            this.openOptionsMenu()
        }

        val viewModelFactory = PaymentViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ManageWalletViewModel::class.java)
        navController = this.findNavController(R.id.nav_host_fragment)

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)
        //viewModel.getAllUsersWalletsAsync(id)
        viewModel.getUserRoom()
        viewModel.setProgressBarVisibility(true)

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.addWalletButton.setOnClickListener {
            createNewWalletDialog()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun createNewWalletDialog(){
        //Build alert dialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.create_new_wallet))
        builder.setMessage(getString(R.string.create_wallet_verification_message))
        builder.setPositiveButton(getString(R.string.continue_)){ _, _ ->
            val newFragment = NewWalletButtonSheetDialog(this, getString(R.string.create_new_wallet))
            newFragment.show(this.supportFragmentManager, "QuantityFragment")
        }
        builder.setNegativeButton(getString(R.string.close)) { dialog, _ ->
            //Dismiss dialog on cancel
            dialog.dismiss()
        }.show()
    }

    override fun newWalletClicked(pin: String, action: String) {
        //get user from view model
        val user = viewModel.getUserData().value ?: UserDataLocalModel()
        viewModel.verifyUserPinAsync(pin, user.token, navController, action)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.wallet_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_make_default_wallet -> {
                val newFragment = NewWalletButtonSheetDialog(this, getString(R.string.make_default_wallet))
                newFragment.show(this.supportFragmentManager, "QuantityFragment")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}