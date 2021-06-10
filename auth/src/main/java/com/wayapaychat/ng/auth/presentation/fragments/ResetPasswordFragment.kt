package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.auth.BR
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentResetPasswordBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import com.wayapaychat.ng.auth.presentation.fragments.forgotPassword.ResetPasswordFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment :
    BaseFragment<FragmentResetPasswordBinding, AuthenticationActivityViewModel>() {

    lateinit var binding: FragmentResetPasswordBinding

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_reset_password

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: FragmentResetPasswordBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authenticationActivityViewModel.changePassword.observe(viewLifecycleOwner, {
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message -> showSnackBar(binding.root, message, true) }
                }
                WayaAppState.SUCCESS -> {
                    dismissLoadingDialog()
                    it.data?.let {
                        if (it.contains("does not")) {
                            showSnackBar(binding.root, it, true)
                            navigate(WayaNavigationCommand.Back)
                        } else {
                            showSnackBar(binding.root, it, false)
                            navigate(WayaNavigationCommand.To(ResetPasswordFragmentDirections.actionResetPasswordFragmentToSuccessfulFragment()))
                        }
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setObservers()
    }

    private fun setObservers() {
        with(authenticationActivityViewModel) {
            toastMessage.observe(viewLifecycleOwner, EventObserver {
                showSnackBar(binding.root, it, true)
            })
        }
    }
}
