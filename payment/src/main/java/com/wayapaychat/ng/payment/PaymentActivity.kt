package com.wayapaychat.ng.payment

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.payment.databinding.ActivityPaymentBinding
import com.wayapaychat.ng.payment.databinding.PaymentLandingNavHeaderBinding
import com.wayapaychat.ng.payment.main.SideNavAdapter
import com.wayapaychat.ng.payment.model.PaymentViewModel
import com.wayapaychat.ng.payment.utils.getListNavItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), SideNavAdapter.OnClickListener {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var bindingHeader: PaymentLandingNavHeaderBinding
    private lateinit var viewModel: PaymentViewModel
    private lateinit var navAdapter: SideNavAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        bindingHeader = DataBindingUtil.inflate(
            layoutInflater, R.layout.payment_landing_nav_header,
            binding.navView as ViewGroup, true
        )

        val viewModelFactory = PaymentViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PaymentViewModel::class.java)

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)

        viewModel.getGramUserAsync(id.toString())

        //Set SideNav Menu Items Using Recycler View
        navAdapter = SideNavAdapter(this)
        navAdapter.setListItems(getListNavItems(applicationContext))
        val manager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        bindingHeader.recyclerView.apply {
            layoutManager = manager
            adapter = navAdapter
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        bindingHeader.lifecycleOwner = this
        bindingHeader.viewModel = viewModel
    }

    fun openDrawer(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    //On Side drawer options clicked
    override fun onItemClicked(position: Int) {
        //close nav drawer
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        val item = navAdapter.getItem(position)
        when (item.item) {
            getString(R.string.my_account) -> {
                startActivity(AppActivityNavCommands.getProfileIntent(this))
            }
            getString(R.string.discover_people) -> {
            }
            getString(R.string.qr_code) -> {
                startActivity(AppActivityNavCommands.getQrCodeProfileIntent(this))
            }

            else -> Toast.makeText(applicationContext, item.item, Toast.LENGTH_SHORT).show()
        }
    }
}