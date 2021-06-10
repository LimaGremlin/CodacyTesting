package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.Helpers.loadImageFromUrl
import com.wayapaychat.core.utils.copyToClipboard
import com.wayapaychat.core.utils.hide
import com.wayapaychat.core.utils.show
import com.wayapaychat.core.utils.showShortToast
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentProfileLandingBinding
import com.wayapaychat.ng.profile.presentation.viewModels.ProfileLandingViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.share


@AndroidEntryPoint
class ProfileLandingFragment :
    BaseFragment<FragmentProfileLandingBinding, ProfileLandingViewModel>(),
    View.OnClickListener {

    lateinit var binding: FragmentProfileLandingBinding

    private val profileLandingViewModel: ProfileLandingViewModel by viewModels()

    override fun getViewModel(): ProfileLandingViewModel = profileLandingViewModel

    override fun getLayoutId(): Int = R.layout.fragment_profile_landing

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentProfileLandingBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupClickListeners()
        setupObservers()
        profileLandingViewModel.getProfileDetails()

        binding.qrCodeButton.setOnClickListener {
            val intent  = AppActivityNavCommands.getQrCodeProfileIntent(requireContext())
            //intent.putExtra(IntentBundles.GRAM_PROFILE_KEY, user)
            startActivity(intent)
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            editProfileInclude.root.setOnClickListener(this@ProfileLandingFragment)
            getReferralCodeInclude.root.setOnClickListener(this@ProfileLandingFragment)
            editPasswordInclude.root.setOnClickListener(this@ProfileLandingFragment)
            loginInformationInclude.root.setOnClickListener(this@ProfileLandingFragment)
        }
    }

    private fun setupObservers() {
        with(profileLandingViewModel) {
            getReferralCodeLiveData.observe(viewLifecycleOwner, Observer {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { showSnackBar(binding.root, it, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle(R.string.dialog_referral_code_title)
                            setMessage(it.data)

                            setPositiveButton(R.string.share_title) { _, _ ->
                                it.data?.let {
                                    requireContext().share(
                                        it,
                                        getString(R.string.share_referral_code_subject)
                                    )
                                }
                            }
                            setNegativeButton(R.string.copy) { _, _ ->
                                it.data?.let {
                                    requireContext().copyToClipboard(
                                        getString(R.string.label_referral_code_clipboard),
                                        it
                                    )
                                    requireContext().showShortToast(getString(R.string.toast_referral_code_copied))
                                }
                            }
                        }.show()
                    }
                }
            })

            profileDetailsLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> binding.profilePicPB.show()

                    WayaAppState.FAILED -> binding.profilePicPB.hide()

                    WayaAppState.SUCCESS -> {
                        binding.profilePicPB.hide()

                        it.data?.profileImage?.let {
                            if (it.isNotEmpty()) {
                                binding.profilePic.loadImageFromUrl(
                                    it, requireContext()
                                )
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.edit_profile_include -> {
                findNavController().navigate(R.id.action_profileLandingFragment_to_editProfileFragment)
            }

            R.id.get_referral_code_include -> {
                profileLandingViewModel.getReferralCode()
            }

            R.id.edit_password_include -> {
                findNavController().navigate(R.id.action_profileLandingFragment_to_sendOtpForChangePasswordFragment)
            }

            R.id.login_information_include -> {
                findNavController().navigate(R.id.action_profileLandingFragment_to_loginInformationFragment)
            }

        }
    }

}
