package com.wayapaychat.ng.payment.fragments.bottom_sheet

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.managewallet.ManageWalletViewModel


class BankTransferBottomSheet(private val context: Activity) : BottomSheetDialogFragment() {
    lateinit var back_btn: ImageView
    private lateinit var viewModel: ManageWalletViewModel
    private lateinit var copy: ImageView
    private lateinit var bank_account: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(
            R.layout.fragment_bank_transfer_bottom_sheet,
            container,
            false
        )
        back_btn = view.findViewById(R.id.back_btn)
        copy = view.findViewById(R.id.copy)
        bank_account = view.findViewById(R.id.bank_acct_txt)

        controls()
        return view

    }

    private fun controls() {
        val rubiesBankAccount = "0100000009"
        val accountText = "$rubiesBankAccount /Rubies MFB"
        bank_account.text = accountText
        copy.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Account", accountText)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                context, "Copied to clipboard!",
                Toast.LENGTH_SHORT,
            ).show()

        }

        back_btn.setOnClickListener {
            dialog?.dismiss()
        }
    }
    
}