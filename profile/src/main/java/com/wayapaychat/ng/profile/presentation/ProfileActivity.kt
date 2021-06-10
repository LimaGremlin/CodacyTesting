package com.wayapaychat.ng.profile.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.wayapaychat.core.base.BaseActivity

import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>() {

    private lateinit var binding: ActivityProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_profile

    override fun getViewModel(): ProfileViewModel = profileViewModel

    override fun getBindingVariable(): Int = 0

    override fun getBinding(binding: ActivityProfileBinding) {
        this.binding = binding
    }
}
