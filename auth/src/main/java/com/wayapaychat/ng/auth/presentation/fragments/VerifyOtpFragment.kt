package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.newtwork.PersonalUser
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.auth.BR
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.VerifyOtpBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpFragment : BaseFragment<VerifyOtpBinding, AuthenticationActivityViewModel>(),
    View.OnClickListener {

    private lateinit var binding: VerifyOtpBinding

    private val authViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.verifyPhoneOtp.observe(viewLifecycleOwner, EventObserver {
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message -> showSnackBar(binding.root, message, true) }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpObservers()

        binding.signInBtn.setOnClickListener {
            authViewModel.verifyUserOtp(binding.pinInput.text.toString(), findNavController())
        }

        binding.didNotGetOtp.setOnClickListener {
            val user = authViewModel.getPersonalUserAsync().value ?: PersonalUser()
            authViewModel.resendOTPAsync(user.phoneNumber, user.email)
        }
    }

    private fun setUpObservers() {
        with(authViewModel) {
            resendOtpToPhoneLiveData.observe(viewLifecycleOwner, EventObserver {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()
                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message ->
                            showSnackBar(binding.root, message, true)
                        }
                    }
                }
            })
        }
    }

    override fun getViewModel(): AuthenticationActivityViewModel = authViewModel

    override fun getLayoutId(): Int = R.layout.verify_otp

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: VerifyOtpBinding) {
        this.binding = binding
    }

    override fun onClick(v: View?) {
        //TODO("Not yet implemented")
    }
}
