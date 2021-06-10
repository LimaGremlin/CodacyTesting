package com.wayapay.ng.landingpage.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.DialogNewPostBinding
import com.wayapay.ng.landingpage.models.SelectOptions

class NewPostBottomSheet(private val onItemClicked: OnItemClicked): BottomSheetDialogFragment() {

    private lateinit var binding: DialogNewPostBinding

    interface OnItemClicked{
        fun onOptionClicked(option:String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_new_post, container, false)

        binding.auctionOption.setOnClickListener {
            onItemClicked.onOptionClicked(SelectOptions.DONATION)
            dismiss()
        }

        binding.postOption.setOnClickListener {
            onItemClicked.onOptionClicked(SelectOptions.POST)
            dismiss()
        }

        binding.eventOption.setOnClickListener {
            onItemClicked.onOptionClicked(SelectOptions.EVENTS)
            dismiss()
        }

        binding.groupOption.setOnClickListener {
            onItemClicked.onOptionClicked(SelectOptions.GROUP)
            dismiss()
        }

        binding.momentOption.setOnClickListener {
            onItemClicked.onOptionClicked(SelectOptions.MOMENTS)
            dismiss()
        }

        binding.pagesOption.setOnClickListener {
            onItemClicked.onOptionClicked(SelectOptions.PAGES)
            dismiss()
        }

        return binding.root
    }
}