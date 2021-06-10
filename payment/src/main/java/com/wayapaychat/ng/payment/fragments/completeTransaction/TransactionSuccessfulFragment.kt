package com.wayapaychat.ng.payment.fragments.completeTransaction

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentTransactionSuccessfulBinding
import com.wayapaychat.ng.payment.utils.TRANSACTION_AMOUNT
import com.wayapaychat.ng.payment.utils.TRANSACTION_USER_NAME
import com.wayapaychat.ng.payment.viewModels.TransactionSuccessfulViewModel

class TransactionSuccessfulFragment : BaseFragment<FragmentTransactionSuccessfulBinding, TransactionSuccessfulViewModel>() {

    private var amount: Int? = null
    private var beneficiary: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            amount = it.getInt(TRANSACTION_AMOUNT)
            beneficiary = it.getString(TRANSACTION_USER_NAME)
        }
    }

    private val transactionSuccessfulViewModel: TransactionSuccessfulViewModel by viewModels()
    private lateinit var binding: FragmentTransactionSuccessfulBinding

    override fun getViewModel(): TransactionSuccessfulViewModel =
        transactionSuccessfulViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_transaction_successful

    override fun getBindingVariable(): Int =
        0

    override fun getLayoutBinding(binding: FragmentTransactionSuccessfulBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        amount?.let {
            binding.amountTV.text = getString(R.string.placeholder_amount_being_sent_to, it.toString())
        }

        beneficiary?.let {
            binding.receiverTV.text = it
        }

        binding.finishMB.setOnClickListener {
            findNavController().popBackStack(R.id.QRCodeFragment, true)
        }

    }

}