package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentPinSetBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinSetFragment : BaseFragment<FragmentPinSetBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: FragmentPinSetBinding

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_pin_set

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentPinSetBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()

        binding.contBtn.setOnClickListener {
            requireActivity().startActivity(AppActivityNavCommands.getPaymentIntent(requireContext().applicationContext))
            requireActivity().finish()
        }
    }

    private fun setOnClickListeners() {
    }
}
