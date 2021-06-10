package com.wayapaychat.ng.payment.transferFunds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentTransferFundsBinding

class TransferFundsBottomSheet (private val onItemClicked: OnItemClicked): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTransferFundsBinding

    interface OnItemClicked{
        fun onOptionClicked(option:String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_transfer_funds, container, false)

        binding.payToEmailOption.setOnClickListener {

            dismiss()
        }

        binding.payWayaIdOption.setOnClickListener {

            dismiss()
        }

        binding.scanToPayOption.setOnClickListener {

            dismiss()
        }

        binding.payToPhoneOption.setOnClickListener {
            dismiss()
        }

        binding.sendToBenefiOption.setOnClickListener {
            dismiss()
        }


        return binding.root
    }
}