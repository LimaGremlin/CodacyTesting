package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.Helpers.isEmailValid
import com.wayapaychat.core.utils.Helpers.isValidPhone
import com.wayapaychat.core.utils.Helpers.replaceWithCountryCode
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentResendOtpBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResendOtpFragment :
    BaseFragment<FragmentResendOtpBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: FragmentResendOtpBinding

    private val authViewModel: AuthenticationActivityViewModel by activityViewModels()


    override fun getViewModel(): AuthenticationActivityViewModel = authViewModel

    override fun getLayoutId(): Int = R.layout.fragment_resend_otp

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentResendOtpBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.resendOtpToPhoneLiveData.observe(viewLifecycleOwner, EventObserver {
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message ->
                        showSnackBar(binding.root, message, true)
                    }
                }
                WayaAppState.SUCCESS -> {
                    dismissLoadingDialog()
                    it.data?.let { userSentOtp ->
                        if (userSentOtp) {
                            navigate(
                                WayaNavigationCommand.To(
                                    ResendOtpFragmentDirections.actionResendOtpFragmentToVerifyOtpFragment(
                                        "This is a different screen",
                                        true
                                    )
                                )
                            )
                        }
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListener()
        watchTextChange()
    }

    private fun setupListener() {
        with(binding) {
            loginBtn.actionBtn.setOnClickListener {
                authViewModel.resendOtpToPhone(
                    binding.identifier.text.toString(),
                    binding.phone.text.toString().replaceWithCountryCode()
                )
            }
            topBar.back.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun checkFormState() {
        if (!binding.identifier.text.toString().isEmailValid() ||
            !binding.phone.text.toString().isValidPhone()
        ) {
            binding.loginBtn.actionBtn.deactivate()
        } else {
            binding.loginBtn.actionBtn.activate()
        }
    }

    private fun watchTextChange() {
        binding.identifier.addTextChangedListener {
            checkFormState()
            if (!it.toString().isEmailValid()) {
                binding.identifier.error = "Not a valid email"
            }
        }

        binding.phone.addTextChangedListener {
            checkFormState()
            if (!it.toString().isValidPhone()) {
                binding.phone.error = "Not a valid phone number"
            }
        }

    }
}
