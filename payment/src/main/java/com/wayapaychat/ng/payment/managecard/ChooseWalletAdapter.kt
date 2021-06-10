package com.wayapaychat.ng.payment.managecard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.core.models.TempWallet
import com.wayapaychat.ng.payment.databinding.ListWalletBinding

class ChooseWalletAdapter(private val onClickListener: OnWalletClickListener,
                    private val TAG: String = "TAG"): RecyclerView.Adapter<ChooseWalletAdapter.MyViewHolder>() {

    interface OnWalletClickListener{
        fun onWalletClicked(position: Int, tag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListWalletBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listWallet.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    var listWallet = mutableListOf<TempWallet>()
    fun getWallet(position: Int) = listWallet[position]

    inner class MyViewHolder(private val binding: ListWalletBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            val wallet = getWallet(position)
            binding.wallet = wallet

            binding.root.setOnClickListener {
                onClickListener.onWalletClicked(position, TAG)
            }
        }

    }

    fun swapList(newList: List<TempWallet>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listWallet, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        //update the list fo reviews
        listWallet = newList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<TempWallet>,
                                   private val newList: List<TempWallet>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList == newList
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        @Nullable
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}