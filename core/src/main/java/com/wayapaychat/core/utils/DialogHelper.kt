package com.wayapaychat.core.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.wayapaychat.core.R
import com.wayapaychat.core.databinding.CustomProgressBinding
import com.wayapaychat.core.databinding.MessageDialogTemplateBinding
import com.wayapaychat.core.utils.Helpers.hide
import com.wayapaychat.core.utils.Helpers.reveal
import java.util.*

object DialogHelper {

    fun showDialog(
        context: Context, cancelable: Boolean, message: String, title: String,
        type: Int, showClear: Boolean? = false, action: (() -> Any)? = null
    ) {
        val inflater = LayoutInflater.from(context)
        val binding: MessageDialogTemplateBinding = DataBindingUtil.inflate(
            inflater, R.layout.message_dialog_template, null, false
        )
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(cancelable)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding.title.text = title
        binding.message.text = message

        if (showClear == true) binding.closeBtn.reveal() else binding.closeBtn.hide()

        binding.closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        when (type) {
            1 -> { // from create wallet

                binding.cancelAction.reveal()
                binding.continueAction.reveal()
                binding.dialUssdAction.hide()

                binding.cancelAction.setOnClickListener {
                    dialog.dismiss()
                }

                binding.continueAction.setOnClickListener {
                    action?.invoke()
                }
            }
            2 -> { // just message
                binding.cancelAction.hide()
                binding.continueAction.hide()
                binding.dialUssdAction.hide()
            }
            3 -> { // from transact with ussd
                binding.cancelAction.hide()
                binding.continueAction.hide()
                binding.dialUssdAction.reveal()

                binding.dialUssdAction.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }

        dialog.show()

    }

    fun popupwindowmenu(layout: View, context: Context, anchorView: View): PopupWindow {
        val popupWindow = PopupWindow(context)
        popupWindow.contentView = layout
        popupWindow.showAsDropDown(anchorView)
        return popupWindow
    }

    fun customDialog(context: Context?): Dialog? {
        val dialog = Dialog(context!!, R.style.Theme_AppCompat)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val customProgressBinding: CustomProgressBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_progress,
            null,
            false
        )
        dialog.setContentView(customProgressBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        val layoutParams = dialog.window!!.attributes
        layoutParams.dimAmount = 0.7f
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.attributes = layoutParams
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        return dialog
    }

    fun showMessage(v: View, activity: Activity) {
        v.reveal()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                activity.runOnUiThread {
                    v.hide()
                }
            }

        }, 5000)
    }

    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}
