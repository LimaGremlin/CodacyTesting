package com.wayapaychat.ng.auth.presentation.fragments.forgotPassword

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.wayapaychat.ng.auth.databinding.PinFragmentBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPinFragment : BaseFragment<FragmentResetPinBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: FragmentResetPinBinding
    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_reset_pin

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: FragmentResetPinBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.resetButton.setOnClickListener {
            if(check()) showDialog()
        }
    }

    private fun check():Boolean {
        if(TextUtils.isEmpty(binding.pinInput.text.toString())){
            binding.pinInput.error = ""
            binding.pinInput.requestFocus()
            return false
        }
        if(binding.pinInput.text.toString().length < 3){
            binding.pinInput.error = ""
            binding.pinInput.requestFocus()
            return false
        }
        if(binding.pinConfirmInput.text.toString() != binding.pinInput.text.toString()){
            binding.pinConfirmInput.error = getString(R.string.pin_do_not_match)
            binding.pinConfirmInput.requestFocus()
            return false
        }

        authenticationActivityViewModel.setPin(binding.pinInput.text.toString())

        return true
    }

    private fun showDialog(){
        //Build alert dialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.info))
        builder.setMessage(getString(R.string.a_code_will_be_sent_to_f_e_to_verify_))
        builder.setPositiveButton(getString(R.string.send)){ _, _ ->
            //Call service to send OTP to user
            authenticationActivityViewModel.resetPinOTPAsync(findNavController())
        }
        builder.setNegativeButton(getString(R.string.close)) { dialog, _ ->
            //Dismiss dialog on cancel
            dialog.dismiss()
        }.show()
    }
}