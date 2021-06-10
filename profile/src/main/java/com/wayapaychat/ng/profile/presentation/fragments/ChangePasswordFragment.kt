package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.Helpers.isEmailValid
import com.wayapaychat.core.utils.Helpers.isValidPassword
import com.wayapaychat.core.utils.Helpers.isValidPhone
import com.wayapaychat.core.utils.hide
import com.wayapaychat.core.utils.show
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentChangePasswordBinding
import com.wayapaychat.ng.profile.presentation.model.profile.ChangePasswordRequest
import com.wayapaychat.ng.profile.presentation.viewModels.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_password.*

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>(), View.OnClickListener {

    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()
    private lateinit var binding: FragmentChangePasswordBinding

    override fun getViewModel(): ChangePasswordViewModel = changePasswordViewModel

    override fun getLayoutId(): Int = R.layout.fragment_change_password

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentChangePasswordBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
        checkFormState()
        watchTextChange()
    }

    private fun setupObservers(){
        with(changePasswordViewModel){
            changePasswordLiveData.observe(viewLifecycleOwner, {
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
                        findNavController().navigate(R.id.action_changePasswordFragment_to_profileLandingFragment)
                    }
                }
            })

            initiateChangePasswordLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> {
                        resend_OTP.setText(R.string.resending_otp)
                        resend_OTP_PB.show()
                    }

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        resend_OTP_PB.hide()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        resend_OTP_PB.hide()
                        resend_OTP.setText(R.string.resend_otp)
                        it.message?.let { message -> showSnackBar(binding.root, message, false) }
                    }
                }
            })
        }
    }

    private fun setupClickListeners(){
        with(binding){
            changePasswordBtn.setOnClickListener(this@ChangePasswordFragment)
            cancelBtn.setOnClickListener(this@ChangePasswordFragment)
            resendOTP.setOnClickListener(this@ChangePasswordFragment)
        }
    }

    private fun watchTextChange(){
        with(binding){

            oldPassword.addTextChangedListener{
                checkFormState()
                if (!it.toString().isValidPassword()) {
                    oldPassword.error = "Not a valid password"
                }
            }

            newPassword.addTextChangedListener{
                checkFormState()
                if (!it.toString().isValidPassword()) {
                    newPassword.error = "Not a valid password"
                }
            }

            confirmPassword.addTextChangedListener{
                checkFormState()
                if (!it.toString().isValidPassword()) {
                    confirmPassword.error = "Not a valid password"
                }else if(it.toString() != newPassword.text.toString()){
                    confirmPassword.error = "Passwords don't match"
                }
            }

            otp.addTextChangedListener{
                checkFormState()
                if (it.toString().length < 6) {
                    otp.error = "Not a valid OTP"
                }
            }

        }
    }

    private fun checkFormState() {
        if (!binding.oldPassword.text.toString().isValidPassword() ||
            !binding.newPassword.text.toString().isValidPassword() ||
            !binding.confirmPassword.text.toString().isValidPassword() ||
            binding.newPassword.text.toString() != binding.confirmPassword.text.toString() ||
            binding.otp.text.toString().length < 6) {
            binding.changePasswordBtn.deactivate()
        } else {
            binding.changePasswordBtn.activate()
        }
    }

    override fun onClick(v: View) {
        when(v.id){

            R.id.changePasswordBtn ->
                changePasswordViewModel.changePassword(
                    ChangePasswordRequest(
                        binding.newPassword.text.toString(),
                        binding.oldPassword.text.toString(),
                        binding.otp.text.toString()
                    )
                )

            R.id.cancel_btn ->
                findNavController().navigate(R.id.action_changePasswordFragment_to_profileLandingFragment)

            R.id.resend_OTP ->
                changePasswordViewModel.initiateChangePassword()

        }
    }

}