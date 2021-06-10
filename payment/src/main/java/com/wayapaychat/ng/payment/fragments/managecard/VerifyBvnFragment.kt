package com.wayapaychat.ng.payment.fragments.managecard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.BR
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.VerifyBvnFragmentBinding


class VerifyBvnFragment : BaseFragment<VerifyBvnFragmentBinding, PaymentActivityViewModel>() {
    private lateinit var binding: VerifyBvnFragmentBinding
    private val paymentActivityViewModel: PaymentActivityViewModel by activityViewModels()

    override fun getViewModel(): PaymentActivityViewModel = paymentActivityViewModel

    override fun getLayoutId(): Int = R.layout.verify_bvn_fragment

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paymentActivityViewModel.submitOTP.observe(viewLifecycleOwner, {
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message -> showSnackBar(binding.root, message, true) }
                }
                WayaAppState.SUCCESS -> {
                    dismissLoadingDialog()
                    it.data?.let {
                        if (it) {
                            navigate(
                                WayaNavigationCommand.To(
                                    VerifyBvnFragmentDirections.actionVerifyBvnFragmentToCardVerifiedFragment(
                                        "Card Verified"
                                    )
                                )
                            )
                        }
                    }
                }
            }
        })
    }

    override fun getLayoutBinding(binding: VerifyBvnFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setObservers()
    }

    private fun setObservers() {
        with(paymentActivityViewModel) {
            toastMessage.observe(viewLifecycleOwner, EventObserver {
                showSnackBar(binding.root, it, true)
            })
        }
    }


}
