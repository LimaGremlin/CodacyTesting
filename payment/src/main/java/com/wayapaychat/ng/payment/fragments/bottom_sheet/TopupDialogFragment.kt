package com.wayapaychat.ng.payment.fragments.bottom_sheet

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R


class TopupDialogFragment(private val context: Activity) : BottomSheetDialogFragment() {
    lateinit var card: TextView
    lateinit var bank: TextView
    lateinit var transfer: TextView
    lateinit var ussd: TextView
    lateinit var dial_ussd: TextView
    lateinit var dialogBuilder: AlertDialog.Builder
    lateinit var dialog: AlertDialog

    private val paymentViewModel: PaymentActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_topup_dialog, container, false)
        card = view.findViewById(R.id.tv_card)
        bank = view.findViewById(R.id.tv_bank)
        transfer = view.findViewById(R.id.tv_transfer)
        ussd = view.findViewById(R.id.tv_ussd)

        controls()
        return view
    }

    fun controls() {
        card.setOnClickListener {
            val intent = Intent(activity, TopupActivity::class.java)
            startActivity(intent)
        }
        ussd.setOnClickListener {

            createUSSDDialog()
        }
        transfer.setOnClickListener {

            val transferBottomSheet = BankTransferBottomSheet(context)
            transferBottomSheet.show(requireActivity().supportFragmentManager, "Dialog")
        }

    }

    private fun createUSSDDialog() {
        dialogBuilder = AlertDialog.Builder(context)
        val popupView: View = layoutInflater.inflate(R.layout.popup, null)

        dialogBuilder.setView(popupView)
        dialog = dialogBuilder.create()
        dial_ussd = popupView.findViewById(R.id.dial_ussd);
        dial_ussd.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:*131*")
            if (i.resolveActivity(context.packageManager) != null) {
                 context.startActivity(i)
            }

        }
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.show()
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss() // dismiss the dialog

            }
            true
        }
    }

}