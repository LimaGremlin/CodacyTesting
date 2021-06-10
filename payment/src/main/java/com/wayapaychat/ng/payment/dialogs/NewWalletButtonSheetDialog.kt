package com.wayapaychat.ng.payment.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.NewWalletBottomSheetBinding

class NewWalletButtonSheetDialog(private val onClick: OnClickListener,
                                 private val action: String): BottomSheetDialogFragment() {

    private lateinit var binding: NewWalletBottomSheetBinding

    interface OnClickListener{
        fun newWalletClicked(pin: String, action: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.new_wallet_bottom_sheet, container, false)

        binding.submitButton.text = action
        binding.submitButton.setOnClickListener {
            onClick.newWalletClicked(binding.pinInput.text.toString(), action)
            dismiss()
        }

        return binding.root
    }
}