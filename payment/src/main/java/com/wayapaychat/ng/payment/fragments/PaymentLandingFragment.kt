package com.wayapaychat.ng.payment.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.Helpers.showSideItems
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.payment.*
import com.wayapaychat.ng.payment.databinding.FragmentPaymentLandingBinding
import com.wayapaychat.ng.payment.model.Wallet
import com.wayapaychat.ng.payment.utils.WalletsAdapter

class PaymentLandingFragment :
    BaseFragment<FragmentPaymentLandingBinding, PaymentActivityViewModel>(), View.OnClickListener {

    private lateinit var binding: FragmentPaymentLandingBinding
    lateinit var adapter: WalletsAdapter
    private var walletLists = ArrayList<Wallet>()

    private val paymentViewModel: PaymentActivityViewModel by activityViewModels()

    override fun getViewModel(): PaymentActivityViewModel = paymentViewModel

    override fun getLayoutId(): Int = R.layout.fragment_payment_landing

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentPaymentLandingBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getWallets()
        setupListeners()
        setOnClickListeners()
        setObservers()

        //Navigate to Notification fragment
        binding.historyImage.setOnClickListener {
            findNavController().navigate(R.id.action_paymentLandingFragment_to_notificationFragment)
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            vertImage.setOnClickListener { vertView ->
                PopupMenu(vertView.context, vertView).apply {
                    inflate(R.menu.wayapay_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.payment_menu_id -> {
                                true
                            }
                            R.id.manage_wallets -> {
                                val intent = Intent(requireActivity(), ManageWalletActivity::class.java)
                                startActivity(intent)
                                true
                            }
                            R.id.manage_card -> {
                                val intent = Intent(requireActivity(), ManageCardActivity::class.java)
                                startActivity(intent)
                                true
                            }
                            R.id.manage_bank_account -> {
                                val intent = Intent(requireActivity(), ManageBankActivity::class.java)
                                startActivity(intent)
                                true
                            }
                            R.id.manage_wallet -> {
                                startActivity(
                                    AppActivityNavCommands.getWalletIntent(
                                        requireContext()
                                    )
                                )
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

    private fun setupListeners() {
        binding.transfer.setOnClickListener(this)
        binding.withdraw.setOnClickListener(this)
        binding.receive.setOnClickListener(this)
        binding.airtime.container.setOnClickListener(this)
        binding.paymentRequest.setOnClickListener(this)
        binding.betting.container.setOnClickListener(this)
        binding.utiltiyBills.container.setOnClickListener(this)
        binding.data.container.setOnClickListener(this)
        binding.cable.container.setOnClickListener(this)
        binding.linkBankContainer.setOnClickListener(this)
        binding.linkBvnContainer.setOnClickListener(this)
        binding.linkCardContainer.setOnClickListener(this)
        binding.navButton.setOnClickListener(this)
    }

    private fun getWallets() {
        adapter = WalletsAdapter(this.requireContext(), this.requireActivity(), this.parentFragmentManager)
        binding.slider.adapter = adapter
        binding.itemTab.setupWithViewPager(binding.slider)

        binding.slider.showSideItems()

        binding.slider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val wallet = walletLists[position]
//                binding.defaultWalletSwitch.isChecked = wallet.default!!
            }
        })
    }

    private fun setObservers() {
//        paymentViewModel.userWallets.observe(viewLifecycleOwner, {
//            it.let {
//                if (it.isNotEmpty()) {
//                    walletLists = it
//                    adapter.setDataList(walletLists)
//                }
//            }
//        })
        paymentViewModel.userWallets.observe(viewLifecycleOwner, Observer{
            when (it.state) {
                //WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    //dismissLoadingDialog()
                    it.message?.let { message ->
                        showSnackBar(binding.root, message, true)
                    }
                }
                WayaAppState.SUCCESS -> {
                    //dismissLoadingDialog()
                    setWalletData(it.data)
                }
            }
        })
    }

    private fun setWalletData(data: List<Wallet>?) {
        data?.let {
            if (it.isNotEmpty()) {
                walletLists = it as ArrayList<Wallet>
                adapter.setDataList(walletLists)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.transfer -> {
                val btnsheet = layoutInflater.inflate(R.layout.fragment_transfer_funds, null)
                val dialog = BottomSheetDialog(this.requireContext())
                dialog.setContentView(btnsheet)
                btnsheet.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
            binding.withdraw -> {
//                val action =
//                    WayaPayFragmentDirections.actionWayaPayFragmentToWithdrawFundsFragment()
//                Navigation.findNavController(p0).navigate(action)
            }
            binding.receive -> {
//                Navigation.findNavController(p0)
//                    .navigate(Uri.parse("wayapay://payment/receive_funds"))
            }
            binding.paymentRequest -> {
//                Navigation.findNavController(p0)
//                    .navigate(Uri.parse("wayapay://payment/request_payment"))
            }
            binding.airtime.container -> {
//                Navigation.findNavController(p0)
//                    .navigate(Uri.parse("wayapay://payment/airtime_purchase"))
            }

            binding.betting.container -> {
//                Navigation.findNavController(p0).navigate(Uri.parse("wayapay://payment/betting"))
            }

            binding.utiltiyBills.container -> {
//                Navigation.findNavController(p0)
//                    .navigate(Uri.parse("wayapay://payment/utility_bills"))
            }

            binding.data.container -> {
//                Navigation.findNavController(p0).navigate(Uri.parse("wayapay://payment/data"))
            }

            binding.cable.container -> {
//                Navigation.findNavController(p0).navigate(Uri.parse("wayapay://payment/cable"))
            }

            binding.linkBankContainer -> {
                val intent = Intent(requireActivity(), ManageBankActivity::class.java)
                startActivity(intent)
            }
            binding.linkBvnContainer -> {
                val intent = Intent(requireActivity(), LinkBVNActivity::class.java)
                startActivity(intent)
            }
            binding.linkCardContainer -> {
                val intent = Intent(requireActivity(), ManageCardActivity::class.java)
                startActivity(intent)
            }
            binding.navButton -> {
                (requireActivity() as PaymentActivity).openDrawer()
            }
        }
    }
}
