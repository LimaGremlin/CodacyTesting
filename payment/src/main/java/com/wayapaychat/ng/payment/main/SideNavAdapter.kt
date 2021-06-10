package com.wayapaychat.ng.payment.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.ng.payment.databinding.ListPaymentSideNavItemsBinding
import com.wayapaychat.ng.payment.utils.NavItem

class SideNavAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<SideNavAdapter.MyViewHolder>() {

    private var listItems = listOf<NavItem>()

    interface OnClickListener{
        fun onItemClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListPaymentSideNavItemsBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.onBind(position)

    fun getItem(position: Int): NavItem = listItems[position]

    fun setListItems(list: List<NavItem>){
        listItems = list
    }

    inner class MyViewHolder(private val binding: ListPaymentSideNavItemsBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(position: Int){
            val item = getItem(position)
            binding.root.setOnClickListener {
                onClickListener.onItemClicked(position)
            }

            binding.imageView.setImageResource(item.drawableId)
            binding.navItem = item
        }
    }
}