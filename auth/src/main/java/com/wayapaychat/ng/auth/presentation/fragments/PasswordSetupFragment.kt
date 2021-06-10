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
import com.wayapaychat.core.utils.Helpers.isValidPassword
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.PasswordSetupFragmentBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordSetupFragment :
    BaseFragment<PasswordSetupFragmentBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: PasswordSetupFragmentBinding
    var valid = true

    private val authViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authViewModel

    override fun getLayoutId(): Int = R.layout.password_setup_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: PasswordSetupFragmentBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.registerUserLiveData.observe(viewLifecycleOwner, EventObserver {
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message -> showSnackBar(binding.root, message, true) }
                }
                WayaAppState.SUCCESS -> {
                    dismissLoadingDialog()
                    navigate(
                        WayaNavigationCommand.To(
                            PasswordSetupFragmentDirections.actionPasswordSetupFragmentToVerifyOtpFragment(
                                "Verify your Account",
                                true
                            )
                        )
                    )
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        watchTextChange()
        checkFormState()

        valid = false

        binding.loginBtn.setOnClickListener {
            val isMerchant = authViewModel.getIsMerchant().value ?: false
            if(!isMerchant){
                authViewModel.registerPersonalUser(binding.password.text.toString(), findNavController())
            }else {

            }
        }
    }

    private fun setObservers() {
        with(authViewModel) {

        }
    }

    private fun checkFormState() {
        if (!binding.password.text.toString().isValidPassword() ||
            binding.confirmPassword.text.toString() != binding.password.text.toString()
        ) {
            binding.loginBtn.deactivate()
            valid = false
        } else {
            valid = true
            binding.loginBtn.activate()
        }
    }

    private fun watchTextChange() {
        binding.password.addTextChangedListener {
            if (!it.toString().isValidPassword()) {
                binding.password.error = "Please enter a valid password"
            } else {
                checkFormState()
            }
        }

        binding.confirmPassword.addTextChangedListener {
            if (it.toString() != binding.password.text.toString()) {
                binding.confirmPassword.error = "Password does not match"
            } else {
                checkFormState()
            }
        }
    }
}
