package com.wayapaychat.ng.auth.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.auth.BR
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.CreatePinFragmentBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import com.wayapaychat.ng.auth.presentation.model.UserSignUpDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePinFragment :
    BaseFragment<CreatePinFragmentBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: CreatePinFragmentBinding

    private val authViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authViewModel

    override fun getLayoutId(): Int = R.layout.create_pin_fragment

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: CreatePinFragmentBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.userPinCreated.observe(viewLifecycleOwner, EventObserver {
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
        setUpSharedPreference()

        binding.contBtn.setOnClickListener {
            if(check())authViewModel.createPin(binding.pinInput.text.toString(),requireActivity(),  findNavController())
        }
    }

    /**
     * This user Id is vital for WayaGram Activitiy
     */
    private fun setUpSharedPreference(){
        val user = authViewModel.getUser().value ?: UserSignUpDetails()
        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with (sharedPref.edit()) {
            putInt(getString(R.string.user_id_key), user.userId)
            apply()
        }
    }

    private fun setUpObservers() {
        with(authViewModel) {
            toastMessage.observe(viewLifecycleOwner, EventObserver {
                showSnackBar(binding.root, it, false)
            })
        }
    }

    private fun check():Boolean{
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
        return true
    }
}
