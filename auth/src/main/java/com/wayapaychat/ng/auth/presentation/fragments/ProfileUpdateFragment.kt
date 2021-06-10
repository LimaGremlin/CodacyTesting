package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.getValueOrEmptyString
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentProfileUpdateBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel

class ProfileUpdateFragment :
    BaseFragment<FragmentProfileUpdateBinding, AuthenticationActivityViewModel>() {

    lateinit var binding: FragmentProfileUpdateBinding

    private val authViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authViewModel

    override fun getLayoutId(): Int = R.layout.fragment_profile_update

    override fun getBindingVariable(): Int = 0

    var genderOptions = arrayListOf("Male", "Female", "Other")

    override fun getLayoutBinding(binding: FragmentProfileUpdateBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.updateUserProfile.observe(viewLifecycleOwner, {
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message ->
                        showSnackBar(binding.root, message, true)
                    }
                }
                WayaAppState.SUCCESS -> {
                    dismissLoadingDialog()
//                    Toast.makeText(
//                        requireContext(),
//                        "Profile Updated Successfully",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    showSnackBar(binding.root, "Profile Updated Successfully", false)
                    //findNavController().navigate(R.id.action_profileUpdateFragment_to_customizeLandingPages)
                    startPaymentModule()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authViewModel.getUserProfileDetails()
        setOnClickListeners()
        setObservers()
        setupGenderAdapter()

        binding.cancelTextBtn.setOnClickListener {
            //findNavController().navigate(R.id.action_profileUpdateFragment_to_customizeLandingPages)
            startPaymentModule()
        }
    }

    private fun startPaymentModule() {
        startActivity(AppActivityNavCommands.getPaymentIntent(requireContext().applicationContext))
        requireActivity().finish()
    }

    private fun setupGenderAdapter() {
        val genderAdapter = ArrayAdapter(
            requireActivity(), R.layout.spinner_adapter_item, genderOptions
        )
        binding.genderEditText.setAdapter(genderAdapter)
    }

    private fun setObservers() {
        with(authViewModel) {
            getUserProfile.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()
                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message ->
                            showSnackBar(binding.root, message, true)
                        }
                    }
                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        setupUIData(it.data)
                    }
                }
            })
        }
    }

    private fun setupUIData(data: UserDomainModel?) {
        data?.let {
            with(binding) {
                firstNameEditText.setText(it.firstName.getValueOrEmptyString())
                middleNameEditText.setText(it.middleName.getValueOrEmptyString())
                dateOfBirthEditText.setText(it.dateOfBirth.getValueOrEmptyString())
                genderEditText.setText(it.gender.getValueOrEmptyString())
                districtStateEditText.setText(it.district.getValueOrEmptyString())
                addressEditText.setText(it.address.getValueOrEmptyString())
                lastNameEditText.setText(it.surname.getValueOrEmptyString())
                emailEditText.setText(it.email.getValueOrEmptyString())
                phoneNumberEditText.setText(it.phoneNumber.getValueOrEmptyString())
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            saveTextBtn.setOnClickListener {
                with(binding) {
                    authViewModel.updateUserProfile(
                        firstName = firstNameEditText.text.toString(),
                        lastName = lastNameEditText.text.toString(),
                        dob = dateOfBirthEditText.text.toString(),
                        gender = genderEditText.text.toString(),
                        district = districtStateEditText.text.toString(),
                        address = addressEditText.text.toString(),
                        emailAddress = emailEditText.text.toString(),
                        phoneNumber = phoneNumberEditText.text.toString(),
                        middleName = middleNameEditText.text.toString()
                    )
                }
//                navigate(WayaNavigationCommand.To(ProfileUpdateFragmentDirections.actionProfileUpdateFragmentToCustomizeLandingPages()))
            }
        }
    }
}
