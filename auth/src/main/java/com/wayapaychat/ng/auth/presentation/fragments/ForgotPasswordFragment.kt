package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentForgotPasswordBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import com.wayapaychat.ng.auth.presentation.fragments.forgotPassword.ForgotPasswordFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding, AuthenticationActivityViewModel>() {

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    lateinit var binding: FragmentForgotPasswordBinding

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId() = R.layout.fragment_forgot_password

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentForgotPasswordBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authenticationActivityViewModel.forgotPassword.observe(viewLifecycleOwner, {
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message -> showSnackBar(binding.root, message, true) }
                }
                WayaAppState.SUCCESS -> {
                    dismissLoadingDialog()
                    it.data?.let {
                        navigate(WayaNavigationCommand.To(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToOneTimePasswordFragment()))
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
//        setObservers()
    }

    private fun setObservers() {
        with(authenticationActivityViewModel) {
            forgotPassword.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()
                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }
                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        it.data?.let {
                            navigate(WayaNavigationCommand.To(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToOneTimePasswordFragment()))
                        }
                    }
                }
            })
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            nextBtn.setOnClickListener {
                authenticationActivityViewModel.validateEmail(forgotEmail.text.toString())
            }
            backBtn.setOnClickListener {
                navigate(WayaNavigationCommand.Back)
            }
        }
    }
}
