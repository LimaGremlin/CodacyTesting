package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.PaymentSettingsFragmentBinding
import com.wayapaychat.ng.profile.presentation.ProfileViewModel

class PaymentSettings : BaseFragment<PaymentSettingsFragmentBinding, ProfileViewModel>() {
    private lateinit var binding: PaymentSettingsFragmentBinding

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun getViewModel(): ProfileViewModel = profileViewModel

    override fun getLayoutId(): Int = R.layout.payment_settings_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: PaymentSettingsFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            topBar.toolbar.setNavigationOnClickListener {
                navigate(WayaNavigationCommand.Back)
            }
        }
    }
}
