package com.wayapaychat.ng.wallet.presentation

import androidx.activity.viewModels
import com.wayapaychat.core.base.BaseActivity
import com.wayapaychat.ng.wallet.R
import com.wayapaychat.ng.wallet.databinding.ActivityWalletBinding
import com.wayapaychat.ng.wallet.presentation.viewModels.WalletActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletActivity : BaseActivity<ActivityWalletBinding, WalletActivityViewModel>() {

    private val walletActivityViewModel: WalletActivityViewModel by viewModels()
    private lateinit var binding: ActivityWalletBinding

    override fun getLayoutId(): Int =
        R.layout.activity_wallet

    override fun getViewModel(): WalletActivityViewModel =
        walletActivityViewModel

    override fun getBindingVariable(): Int = 0

    override fun getBinding(binding: ActivityWalletBinding) {
        this.binding = binding
    }

}
