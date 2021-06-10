package com.wayapaychat.ng.auth.presentation.fragments.forgotPassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentSuccessfulBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessfulFragment :
    BaseFragment<FragmentSuccessfulBinding, AuthenticationActivityViewModel>() {

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    lateinit var binding: FragmentSuccessfulBinding

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_successful

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentSuccessfulBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            loginBtn.setOnClickListener {
                //navigate back to login-activity
                requireActivity().onBackPressed()
            }
        }
    }
}
