package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.ChatSettingsFragmentBinding
import com.wayapaychat.ng.profile.presentation.ProfileViewModel

class ChatSettingsFragment : BaseFragment<ChatSettingsFragmentBinding, ProfileViewModel>() {
    private lateinit var binding: ChatSettingsFragmentBinding

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun getViewModel(): ProfileViewModel = profileViewModel

    override fun getLayoutId(): Int = R.layout.chat_settings_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: ChatSettingsFragmentBinding) {
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
