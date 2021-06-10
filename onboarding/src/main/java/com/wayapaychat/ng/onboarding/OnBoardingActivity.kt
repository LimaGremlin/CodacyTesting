package com.wayapaychat.ng.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import com.wayapaychat.core.base.BaseActivity
import com.wayapaychat.ng.onboarding.databinding.ActivityOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding, OnBoardingViewModel>() {

    private lateinit var binding: ActivityOnBoardingBinding

    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_on_boarding

    override fun getViewModel(): OnBoardingViewModel = onBoardingViewModel

    override fun getBindingVariable(): Int = 0

    override fun getBinding(binding: ActivityOnBoardingBinding) {
        this.binding = binding
    }
}
