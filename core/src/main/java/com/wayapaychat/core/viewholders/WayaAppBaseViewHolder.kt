package com.wayapaychat.core.viewholders

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class WayaAppBaseViewHolder <T> (
    binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {

    abstract fun bindItem(item: T)
}
