package com.wayapaychat.ng.onboarding.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.onboarding.OnBoardingViewModel
import com.wayapaychat.ng.onboarding.R
import com.wayapaychat.ng.onboarding.databinding.FragmentOnBoardingLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingLandingFragment :
    BaseFragment<FragmentOnBoardingLandingBinding, OnBoardingViewModel>() {

    private lateinit var binding: FragmentOnBoardingLandingBinding

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()

    override fun getViewModel(): OnBoardingViewModel = onBoardingViewModel

    override fun getLayoutId(): Int = R.layout.fragment_on_boarding_landing

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentOnBoardingLandingBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            gettingStarted.setOnClickListener {
                startAuthActivityWithKey("GET_STARTED_KEY")
            }
            login.setOnClickListener {
                startAuthActivityWithKey("LOGIN_KEY")
            }
        }
    }

    private fun startAuthActivityWithKey(key: String) {
        startActivity(AppActivityNavCommands.getAuthIntent(requireContext()).apply {
            putExtra("AUTH_DIRECTION", key)
        })
        requireActivity().finish()
    }
}
