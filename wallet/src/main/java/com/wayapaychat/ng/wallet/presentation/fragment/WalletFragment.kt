package com.wayapaychat.ng.wallet.presentation.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.showLongToast
import com.wayapaychat.core.utils.showShortToast
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.core.utils.toNumberWithCommas
import com.wayapaychat.ng.wallet.R
import com.wayapaychat.ng.wallet.databinding.FragmentWalletBinding
import com.wayapaychat.ng.wallet.models.Wallet
import com.wayapaychat.ng.wallet.presentation.viewModels.WalletActivityViewModel
import com.wayapaychat.ng.wallet.presentation.viewModels.WalletFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment
    : BaseFragment<FragmentWalletBinding, WalletFragmentViewModel>(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private val walletFragmentViewModel by viewModels<WalletFragmentViewModel>()
    private val walletActivityViewModel by activityViewModels<WalletActivityViewModel>()
    private lateinit var binding: FragmentWalletBinding
    private var wallet: Wallet? = null

    private lateinit var popupMenu: PopupMenu

    override fun getViewModel(): WalletFragmentViewModel =
        walletFragmentViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_wallet

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentWalletBinding) {
        this.binding = binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wallet = it.getParcelable(getString(R.string.argument_wallet))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wallet?.let {
            with(binding){
                accountNameTV.text = it.accountName
                accountNumberTV.text = it.accountNo
                balanceTV.text = "N${it.balance.toNumberWithCommas()}"
            }
        }

        setupClickListener()
        setupPopupMenu()
        setupObservers()
    }

    private fun setupObservers(){
        with(walletFragmentViewModel){
            setDefaultWalletLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> {
                        showLoadingDialog()
                    }

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        wallet?.default = true
                        wallet?.accountNo?.let {
                            walletActivityViewModel.walletSetAsDefault(it)
                        }
                        dismissLoadingDialog()
                        showSnackBar(binding.root, getString(R.string.wallet_set_as_default), false)
                    }
                }
            })
        }
    }

    private fun setupClickListener(){
        with(binding){
            searchFilterIV.setOnClickListener(this@WalletFragment)
            moreIV.setOnClickListener(this@WalletFragment)
            backIV.setOnClickListener(this@WalletFragment)
        }
    }

    override fun onClick(v: View) {
        when(v.id){

            R.id.back_IV -> findNavController().popBackStack()

            R.id.more_IV -> popupMenu.show()

        }
    }

    private fun setupPopupMenu(){
        popupMenu = PopupMenu(requireContext(), binding.moreIV).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(this@WalletFragment)
            inflate(R.menu.menu_wallet)

//            val defaultWalletItemMenuItem = menu.findItem(R.id.default_wallet_item)
//            val disableWalletItemMenuItem = menu.findItem(R.id.disable_wallet_item)
//
//            val actionView = disableWalletItemMenuItem.actionView
//            val textView = actionView.findViewById<TextView>(R.id.title_TV)
//            textView.setText(R.string.disable_wallet)
//            val defaultWalletSW = defaultWalletItemMenuItem.actionView.findViewById<SwitchCompat>(R.id.action_SW)
//
//            wallet?.default?.let { isDefault->
//                defaultWalletSW.isChecked = isDefault
//            }
//
//            defaultWalletSW.setOnCheckedChangeListener { _, isChecked ->
//
//            }

        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean =
        when(item.itemId){

            R.id.all_wallets_item -> {
                findNavController().popBackStack()
                true
            }

            R.id.set_default_wallet_item -> {
                if (wallet?.default == true) {
                    requireContext().showLongToast(getString(R.string.already_default_wallet))
                } else {
                    wallet?.accountNo?.let {
                        walletFragmentViewModel.setDefaultWallet(it)
                    }
                }
                true
            }

            else -> false

        }

}