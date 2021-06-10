package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.Helpers.loadImageFromUrl
import com.wayapaychat.core.utils.show
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentSendOtpForChangePasswordBinding
import com.wayapaychat.ng.profile.presentation.viewModels.SendOtpForChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendOtpForChangePasswordFragment
    : BaseFragment<FragmentSendOtpForChangePasswordBinding, SendOtpForChangePasswordViewModel>(), View.OnClickListener {

    private val sendOtpForChangePasswordViewModel: SendOtpForChangePasswordViewModel by viewModels()
    private lateinit var binding: FragmentSendOtpForChangePasswordBinding
    private var email: String? = null

    override fun getViewModel(): SendOtpForChangePasswordViewModel =
        sendOtpForChangePasswordViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_send_otp_for_change_password

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentSendOtpForChangePasswordBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
        if (email == null)
            sendOtpForChangePasswordViewModel.getProfileDetails()
        else
            binding.otpText.text = getString(
                R.string.placeholder_otp_will_be_sent_to_email,
                email
            )
    }

    private fun setupObservers(){
        with(sendOtpForChangePasswordViewModel){
            initiateChangePasswordLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> {
                        showLoadingDialog()
                    }

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        findNavController().navigate(R.id.action_sendOtpForChangePasswordFragment_to_changePasswordFragment)
                    }
                }
            })

            profileDetailsLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> {
                        showLoadingDialog()
                    }

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                        findNavController().popBackStack()
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()

                        it.data?.let { user ->
                            email = user.email
                            binding.otpText.text = getString(
                                R.string.placeholder_otp_will_be_sent_to_email,
                                email
                            )
                        }
                    }
                }
            })
        }
    }

    private fun setupClickListeners(){
        with(binding){
            sendOTPBtn.setOnClickListener(this@SendOtpForChangePasswordFragment)
        }
    }

    override fun onClick(v: View) {
        when(v.id){

            R.id.sendOTPBtn ->
                sendOtpForChangePasswordViewModel.initiateChangePassword()

        }
    }

}