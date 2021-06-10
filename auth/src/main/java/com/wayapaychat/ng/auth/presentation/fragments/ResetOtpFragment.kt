package com.wayapaychat.ng.auth.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.utils.DialogHelper
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.Helpers.isEmailValid
import com.wayapaychat.core.utils.Helpers.isValidPhone
import com.wayapaychat.ng.auth.databinding.FragmentResendOtpBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetOtpFragment : Fragment(), View.OnClickListener {

    private val binding: FragmentResendOtpBinding by lazy {
        FragmentResendOtpBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthenticationActivityViewModel by activityViewModels()

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog = DialogHelper.customDialog(this.requireContext())!!
        setupListener()
//        viewModel.message.observe(viewLifecycleOwner, messageObserver)
//        viewModel.generealResponseData.observe(viewLifecycleOwner, resetOtpObserver)
//        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ResetOtpFragment()
    }


    fun setupListener() {
        binding.loginBtn.actionBtn.setOnClickListener(this)
        binding.topBar.back.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.loginBtn.actionBtn -> {
//                viewModel.resendOtp(
//                    binding.identifier.text.toString(),
//                    binding.phone.text.toString().replaceWithCountryCode()
//                )
            }

            binding.topBar.back -> {
                findNavController().popBackStack()
            }
        }
    }

    fun checkFormState() {
        if (!binding.identifier.text.toString().isEmailValid() ||
            !binding.phone.text.toString().isValidPhone()
        ) {
            binding.loginBtn.actionBtn.deactivate()
        } else {
            binding.loginBtn.actionBtn.activate()
        }
    }

    fun watchTextChange() {
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


    var loadingObserver = Observer<Boolean> {
        when (it) {
            true -> {
                dialog = DialogHelper.customDialog(this.requireContext())!!
            }
            false -> {
                dialog.dismiss()

            }
        }
    }

//    var resetOtpObserver = Observer<GeneralResponse> {
//        if (!it.error) {
//            DialogHelper.showSnackBar(binding.phone, it.message)
//            binding.phone.text.toString()
//                .let { viewModel.setPhoneNumber(it.replaceWithCountryCodeWithPlus()) }
//            binding.identifier.text.toString().let { viewModel.setUserEmail(it) }
//            findNavController().navigate(
//                ResendOtpFragmentDirections.actionResendOtpFragmentToVerifyOtpFragment(
//                    binding.phone.text.toString().replaceWithCountryCodeWithPlus()
//                )
//            )
//        }
//    }

    var messageObserver = Observer<String> {
        if (!it.isNullOrEmpty()) {
            binding.errorMessageBar.warningText.text = it.toString()
            DialogHelper.showMessage(binding.errorMessageBar.container, this.requireActivity())
        }
    }
}
