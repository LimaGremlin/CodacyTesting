package com.wayapaychat.ng.auth.presentation.fragments.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.BR
import com.wayapaychat.ng.auth.databinding.FragmentAuthResendOTPBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import com.wayapaychat.ng.auth.presentation.model.UserSignUpDetails
import com.wayapaychat.ng.auth.presentation.utils.removePlus

/**
 * A simple [Fragment] subclass.
 * Use the [AuthResendOTPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthResendOTPFragment : BaseFragment<FragmentAuthResendOTPBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: FragmentAuthResendOTPBinding
    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_auth_resend_o_t_p

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: FragmentAuthResendOTPBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = authenticationActivityViewModel.getUser().value ?: UserSignUpDetails()
        binding.ccp.registerCarrierNumberEditText(binding.number) //attache phone number edit text to ccp
        binding.otpMessage.text =
            String.format(getString(R.string.resend_otp_email_message_formatter), user.email)

        binding.submitButton.setOnClickListener {
            if(binding.ccp.isValidFullNumber) {
                authenticationActivityViewModel.resendOTPAsync(binding.ccp.formattedFullNumber.removePlus().filter { !it.isWhitespace() }, user.email, findNavController())
            }else binding.number.error = getString(R.string.invalid_number)
        }
    }
}