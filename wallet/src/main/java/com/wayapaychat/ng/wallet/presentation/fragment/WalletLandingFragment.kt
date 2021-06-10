package com.wayapaychat.ng.wallet.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mukesh.OtpView
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.showShortToast
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.wallet.R
import com.wayapaychat.ng.wallet.adapter.WalletAdapter
import com.wayapaychat.ng.wallet.databinding.FragmentWalletLandingBinding
import com.wayapaychat.ng.wallet.models.Wallet
import com.wayapaychat.ng.wallet.presentation.viewModels.WalletActivityViewModel
import com.wayapaychat.ng.wallet.presentation.viewModels.WalletLandingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletLandingFragment : BaseFragment<FragmentWalletLandingBinding, WalletLandingViewModel>() {

    private val walletLandingViewModel: WalletLandingViewModel by viewModels()
    private val walletActivityViewModel by activityViewModels<WalletActivityViewModel>()
    private lateinit var binding: FragmentWalletLandingBinding
    private val walletsAdapter = WalletAdapter {
        walletActivityViewModel.walletSetAsDefault("")
        findNavController().navigate(
            R.id.action_walletLandingFragment_to_walletFragment,
            Bundle().apply {
                putParcelable(getString(R.string.argument_wallet), it)
            }
        )
    }
    private var wallets = mutableListOf<Wallet>()

    override fun getViewModel(): WalletLandingViewModel =
        walletLandingViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_wallet_landing

    override fun getBindingVariable(): Int =
        0

    override fun getLayoutBinding(binding: FragmentWalletLandingBinding) {
        this.binding = binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletLandingViewModel.getWallets()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupObservers()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            topBar.toolbar.setNavigationOnClickListener {
                requireActivity().finish()
            }
        }
    }

    private fun setupObservers() {
        with(walletLandingViewModel) {
            getWalletsLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> {
                        showLoadingDialog()
                    }

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        it.data?.let {
                            wallets = it.toMutableList()
                            binding.walletsRV.adapter = walletsAdapter
                            walletsAdapter.populateDataset(it)
                        }
                    }
                }
            })

            verifyUserPinLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> {
                        showLoadingDialog()
                    }

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        requireContext().showShortToast(it.message ?: "An Error occurred")
                        launchCustomAlertDialog()
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        createWallet()
                    }
                }
            })

            createWalletLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> {
                        showLoadingDialog()
                    }

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        requireContext().showShortToast(it.message ?: "An Error occurred")
                        launchCustomAlertDialog()
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        showSnackBar(binding.root, getString(R.string.wallet_created), false)
                        it.data?.let {
                            wallets.add(it)
                            walletsAdapter.addWallet(it)
                        }
                    }
                }
            })
        }

        with(walletActivityViewModel) {
            walletSetAsDefaultLiveData.observe(viewLifecycleOwner, { accountNo ->
                for (wallet in wallets) {
                    if (wallet.accountNo == accountNo) {
                        wallet.default = true
                    } else if (wallet.default) {
                        wallet.default = false
                    }
                }
                println("Size of wallets is ${wallets.size}")
                walletsAdapter.populateDataset(wallets)
            })
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.topBar.toolbar
        toolbar.inflateMenu(R.menu.menu_wallets)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.create_wallet_item -> {
                    createWalletDialog()
                    true
                }

                else -> false

            }
        }
    }

    private fun createWalletDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.create_wallet_message)
            setMessage(R.string.create_new_wallet)
            setPositiveButton(R.string.yes) { _, _ ->
                launchCustomAlertDialog()
            }
            setNegativeButton(R.string.no, null)
            show()
        }
    }

    private lateinit var verifyPinDialog: AlertDialog

    private fun launchCustomAlertDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        val customAlertDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_verify_pin_for_create_wallet, null, false)
        val pinInput = customAlertDialogView.findViewById<OtpView>(R.id.pin_input)
        customAlertDialogView.findViewById<MaterialButton>(R.id.create_button_MB)
            .setOnClickListener {
                val pin = pinInput.text.toString()
                if (pin.length < 4) {
                    requireContext().showShortToast("Enter a valid Pin")
                } else {
                    verifyPinDialog.dismiss()
                    walletLandingViewModel.verifyUserPin(pin)
                }
            }

        // Building the Alert dialog using materialAlertDialogBuilder instance
        verifyPinDialog =
            materialAlertDialogBuilder.setView(customAlertDialogView)
                .show()
    }
}
