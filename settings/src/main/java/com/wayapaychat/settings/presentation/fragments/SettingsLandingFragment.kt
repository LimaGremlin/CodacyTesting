package com.wayapaychat.settings.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.settings.R
import com.wayapaychat.settings.databinding.FragmentSettingsLandingBinding
import com.wayapaychat.settings.presentation.SettingsActivityViewModel

class SettingsLandingFragment :
    BaseFragment<FragmentSettingsLandingBinding, SettingsActivityViewModel>() {

    lateinit var binding: FragmentSettingsLandingBinding

    private val settingsActivityViewModel: SettingsActivityViewModel by activityViewModels()

    override fun getViewModel(): SettingsActivityViewModel = settingsActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_settings_landing

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentSettingsLandingBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}
