package com.wayapaychat.ng.auth.presentation.fragments.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.ng.auth.BR
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentResetPinBinding
import com.wayapaychat.ng.auth.databinding.FragmentResetPinOTPBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPinOTPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPinOTPFragment : BaseFragment<FragmentResetPinOTPBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: FragmentResetPinOTPBinding
    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_reset_pin_o_t_p

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: FragmentResetPinOTPBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signInBtn.setOnClickListener {
            authenticationActivityViewModel.resetPinPAsync(binding.pinInput.text.toString(), findNavController())
        }
    }
}