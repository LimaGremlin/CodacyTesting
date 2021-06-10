package com.wayapaychat.ng.auth.presentation.fragments.forgotPassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.ng.auth.BR
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentOneTimePasswordBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneTimePasswordFragment :
    BaseFragment<FragmentOneTimePasswordBinding, AuthenticationActivityViewModel>() {

    lateinit var binding: FragmentOneTimePasswordBinding

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_one_time_password

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: FragmentOneTimePasswordBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authenticationActivityViewModel.moveToNewPasswordScreen.observe(
            viewLifecycleOwner,
            EventObserver {
                if (it) {
                    navigate(WayaNavigationCommand.To(OneTimePasswordFragmentDirections.actionOneTimePasswordFragmentToResetPasswordFragment()))
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
        setObservers()
    }

    private fun setObservers() {
        with(authenticationActivityViewModel) {
            toastMessage.observe(viewLifecycleOwner, EventObserver {
                showSnackBar(binding.root, it, true)
            })
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
//            backBtn.setOnClickListener {
//                navigate(WayaNavigationCommand.Back)
//            }
        }
    }
}
