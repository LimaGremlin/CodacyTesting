package com.wayapaychat.core.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.wayapaychat.core.R

@BindingAdapter("inputDrawable", requireAll = true)
fun TextView.setStartDrawable(drawable: Drawable?) {
    drawable?.let {
        setCompoundDrawables(drawable, null, null, null)
    }
}

@BindingAdapter("viewElevation")
fun View.setViewElevation(elevation: Int = 10) {
    ViewCompat.setElevation(this, elevation.toFloat())
}

/**
 * @param position can be 0: Left, 1: Right, 2: Top, 3: Bottom
 */
@BindingAdapter("textDrawable", "drawablePosition")
fun TextView.setImageDrawable(icon: Drawable, position: Int) {
    //DrawableCompat.setTint(icon, ContextCompat.getColor(context, android.R.color.darker_gray))
    when (position) {
        0 -> setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        1 -> setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
        2 -> setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null)
        3 -> setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon)
    }
}

@BindingAdapter("inputType")
fun TextInputEditText.setCustomInputType(type: String) {
    inputType = when (type) {
        "number" -> InputType.TYPE_CLASS_NUMBER
        else -> InputType.TYPE_CLASS_TEXT
    }
}

@BindingAdapter("cardImage")
fun AppCompatImageView.setCardImageFromType(type: String) {
    when (type) {
        "visa " -> this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_visa))
        "master" -> this.setImageDrawable(
            ContextCompat.getDrawable(
                this.context,
                R.drawable.ic_mastercard
            )
        )
        "verve " -> this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_verve))
    }
}

@SuppressLint("DefaultLocale")
@BindingAdapter("statusColor")
fun statusColor(textView: TextView, status: String?) {
    status?.toLowerCase().let { validStatus ->
        when (validStatus) {
            "pending" -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.yellow))
            }
            "approved" -> {
                textView.setTextColor(
                    ContextCompat.getColor(
                        textView.context,
                        R.color.waya_green
                    )
                )
            }
            "declined" -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.red))
            }
            else -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.black))
            }
        }
    }
}

@BindingAdapter("bankImage")
fun AppCompatImageView.setBankImageFromCode(code: String?) {
    when (code) {
        "032 " -> this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_union_bank))
        "033" -> this.setImageDrawable(
                ContextCompat.getDrawable(
                        this.context,
                        R.drawable.ic_uba_bank
                )
        )
        else -> this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_bank))
    }
}

