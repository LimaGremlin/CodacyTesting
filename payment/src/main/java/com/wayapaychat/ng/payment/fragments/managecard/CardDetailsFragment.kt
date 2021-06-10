package com.wayapaychat.ng.payment.fragments.managecard

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.BR
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.CardDetailsFragmentBinding

class CardDetailsFragment : BaseFragment<CardDetailsFragmentBinding, PaymentActivityViewModel>() {
    private val paymentViewModel: PaymentActivityViewModel by activityViewModels()
    private lateinit var binding: CardDetailsFragmentBinding

    override fun getViewModel(): PaymentActivityViewModel = paymentViewModel

    override fun getLayoutId(): Int = R.layout.card_details_fragment

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: CardDetailsFragmentBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paymentViewModel.deleteCard.observe(viewLifecycleOwner, {
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
                    it.data?.let { isDeleted ->
                        if (isDeleted) {
                            showSnackBar(binding.root, "Card Deleted Successfully", false)
                            navigate(WayaNavigationCommand.Back)
                        }
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            deleteCardBtn.setOnClickListener {
                showDeleteDialog()
            }
            toolbar.leftImage.setOnClickListener {
                navigate(WayaNavigationCommand.Back)
            }
        }
    }

    private fun showDeleteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.notice))
            .setMessage(
                "Dear customer, are you sure you want to permanently delete this card?"
            )
            .setPositiveButton(getString(R.string.go_ahead)) { _, _ ->
                paymentViewModel.deleteCard()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }
}
