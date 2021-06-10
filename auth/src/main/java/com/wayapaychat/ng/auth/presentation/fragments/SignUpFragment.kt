package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.newtwork.PersonalUser
import com.wayapaychat.core.utils.Helpers
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.Helpers.hide
import com.wayapaychat.core.utils.Helpers.isEmailValid
import com.wayapaychat.core.utils.Helpers.isValidPhone
import com.wayapaychat.core.utils.Helpers.isVeryEmpty
import com.wayapaychat.core.utils.Helpers.replaceWithCountryCode
import com.wayapaychat.core.utils.Helpers.reveal
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.SignupFragmentBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import com.wayapaychat.ng.auth.presentation.utils.removePlus
import dagger.hilt.android.AndroidEntryPoint
import util.uiModels.PersonalAccountUIModel

@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignupFragmentBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: SignupFragmentBinding
    private var isMerchant = true
    private var valid = true

    private val authViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authViewModel

    override fun getLayoutId(): Int = R.layout.signup_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: SignupFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isMerchant = false
        makeSelection(binding.userBtn)
        val text = binding.termCondition.text.toString()
        Helpers.makeClickableSpan(
            { openTerms() },
            R.color.colorPrimary, false, binding.termCondition,
            text.length - 19, text.length, text
        )

        binding.ccp.registerCarrierNumberEditText(binding.number) //attache phone number edit text to ccp
        //binding.ccp.setCcpClickable(false)

        watchTextChange()
        checkStateGeneral()

        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        with(binding) {
            merchantBtn.setOnClickListener {
                isMerchant = true
                checkStateGeneral()
                makeSelection(binding.merchantBtn)
                binding.businessNameContainer.reveal()
                binding.businessTypeContainer.reveal()
                binding.organizationTypeContainer.reveal()
                binding.firstnameBusinessNameContainer.hide()
                binding.surnameContainer.hide()
                binding.emailContainer.hint = "Organization Email Address"
                binding.hintInputLayout.hint = "Organization Phone Number"
                binding.refererCodeContainer.hint = "Aggregator Code"
            }

            userBtn.setOnClickListener {
                isMerchant = false
                checkStateGeneral()
                makeSelection(binding.userBtn)
                binding.businessNameContainer.hide()
                binding.businessTypeContainer.hide()
                binding.organizationTypeContainer.hide()
                binding.firstnameBusinessNameContainer.reveal()
                binding.surnameContainer.reveal()
                binding.emailContainer.hint = "Email Address"
                binding.hintInputLayout.hint = "Phone Number"
                binding.refererCodeContainer.hint = "Referer Code"
            }

            signUpBtn.actionBtn.setOnClickListener {
                if(binding.ccp.isValidFullNumber) {
                    if (isMerchant) {
                        val action =
                            SignUpFragmentDirections.actionSignUpFragmentToMerchantSecondSignupFragment()
                        Navigation.findNavController(binding.signUpBtn.actionBtn).navigate(action)
                    } else {
                        //Set is merchant globally for all fragments
                        authViewModel.setIsMerchant(isMerchant)
                        //Set PersonalUser Data in ViewModel
                        authViewModel.setPersonalUserAsync(
                            PersonalUser(
                                email = binding.email.text.toString(),
                                firstName = binding.firstnameBusinessName.text.toString(),
                                surname = binding.surname.text.toString(),
                                phoneNumber = binding.ccp.formattedFullNumber.removePlus().filter { !it.isWhitespace() },
                                referenceCode = binding.refererCode.text.toString()
                            )
                        )
                        //Navigate to password fragment
                        val action =
                            SignUpFragmentDirections.actionSignUpFragmentToPasswordSetupFragment()
                        navigate(WayaNavigationCommand.To(action))
                    }
                }else binding.number.error = getString(R.string.invalid_number)
            }
        }
    }

    private fun openTerms() {

    }

    private fun selectButton(button: Button) {
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        button.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.button_plain_bg_white_background)
    }

    private fun unSelectButton(button: Button) {
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        button.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.button_plain_bg_light_blue_background
        )
    }

    private fun makeSelection(button: Button) {
        unSelectButton(binding.merchantBtn)
        unSelectButton(binding.userBtn)
        selectButton(button)
    }

    private fun checkFormState() {
        if (!binding.email.text.toString().isEmailValid() ||
            binding.firstnameBusinessName.text.toString().isEmpty() ||
            binding.surname.text.toString().isEmpty() ||
            !binding.number.text.toString().isValidPhone()
        ) {
            binding.signUpBtn.actionBtn.deactivate()
            valid = false
        } else {
            valid = true
            binding.signUpBtn.actionBtn.activate()
        }
    }

    private fun checkStateGeneral() {
        if (isMerchant) {
            checkFormStateMerchant()
        } else {
            checkFormState()
        }
    }

    private fun checkFormStateMerchant() {
        if (!binding.email.text.toString().isEmailValid() ||
            !binding.number.text.toString().isValidPhone()
        ) {
            binding.signUpBtn.actionBtn.deactivate()
            valid = false
        } else {
            valid = true
            binding.signUpBtn.actionBtn.activate()
        }
    }

    private fun watchTextChange() {
        if (!isMerchant) {
            binding.firstnameBusinessName.addTextChangedListener {
                checkStateGeneral()
                if (!it.toString().isVeryEmpty()) {
                    binding.firstnameBusinessName.error = "This should not be empty"
                }
            }

            binding.surname.addTextChangedListener {
                checkStateGeneral()
                if (!it.toString().isVeryEmpty()) {
                    binding.surname.error = "This should not be empty"
                }
            }
        }
        binding.email.addTextChangedListener {
            checkStateGeneral()
            if (!it.toString().isEmailValid()) {
                binding.email.error = "Not a valid email"
            }
        }

        binding.number.addTextChangedListener {
            checkStateGeneral()
            if (!it.toString().isValidPhone()) {
                binding.number.error = "Not a valid phone number"
            }
        }
    }
}
