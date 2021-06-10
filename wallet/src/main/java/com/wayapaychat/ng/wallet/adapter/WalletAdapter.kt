package com.wayapaychat.ng.wallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.core.utils.toNumberWithCommas
import com.wayapaychat.ng.wallet.databinding.ItemWalletBinding
import com.wayapaychat.ng.wallet.models.Wallet

class WalletAdapter(
    private val navigateToWallet: (wallet: Wallet) -> Unit
): RecyclerView.Adapter<WalletAdapter.Holder>() {

    private var dataset = mutableListOf<Wallet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemWalletBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(dataset[position])
        println("Damn binded")
    }

    override fun getItemCount(): Int =
        dataset.size

    fun populateDataset(dataset: List<Wallet>){
        this.dataset = dataset.toMutableList()
        println("Size of dataset is ${dataset.size}")
        notifyDataSetChanged()
    }

    fun addWallet(wallet: Wallet){
        dataset.add(wallet)
        notifyItemInserted(dataset.size - 1)
    }

    inner class Holder(
        private val binding: ItemWalletBinding
    ): RecyclerView.ViewHolder(binding.root){

        private lateinit var item: Wallet

        init {
            binding.root.setOnClickListener { navigateToWallet(item) }
        }

        fun bind(item: Wallet){
            this.item = item
            with(binding){
                accountNumberTV.text = item.accountNo
                balanceTV.text = "N${item.balance.toNumberWithCommas()}"
            }
        }

    }

}