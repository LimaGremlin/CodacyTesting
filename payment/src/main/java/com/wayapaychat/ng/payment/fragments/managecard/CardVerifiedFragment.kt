package com.wayapaychat.ng.payment.fragments.managecard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.CardVerifiedFragmentBinding


class CardVerifiedFragment : BaseFragment<CardVerifiedFragmentBinding, PaymentActivityViewModel>() {

    val args: CardVerifiedFragmentArgs by navArgs()
    private lateinit var binding: CardVerifiedFragmentBinding
    private val paymentActivityViewModel: PaymentActivityViewModel by activityViewModels()

    override fun getViewModel(): PaymentActivityViewModel = paymentActivityViewModel

    override fun getLayoutId(): Int = R.layout.card_verified_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: CardVerifiedFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getSentData()
        setOnClickListener()
    }

    private fun getSentData() {
        args.let {
            binding.successText.text = it.centerText
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            finishBtn.setOnClickListener {
                navigate(WayaNavigationCommand.Back)
            }
        }
    }
}
