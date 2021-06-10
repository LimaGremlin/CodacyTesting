package com.wayapaychat.ng.payment.managebank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.core.payment.WayaAccounts
import com.wayapaychat.ng.payment.databinding.ListBankAccountsBinding

class BankAccountAdapter(private val onClickListener: OnClickListener,
                         private val TAG: String = "TAG"): RecyclerView.Adapter<BankAccountAdapter.MyViewHolder>(){

    interface OnClickListener{
        fun onAccountClicked(position: Int, tag: String)
    }

    var listAccounts = mutableListOf<WayaAccounts>()
    fun getAccount(position: Int) = listAccounts[position]

    inner class MyViewHolder(private val binding: ListBankAccountsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val accounts = getAccount(position)

            binding.lastFourText.text = accounts.accountNumber.takeLast(4)
            binding.bankCode.text = accounts.bankCode

            binding.root.setOnClickListener {
                onClickListener.onAccountClicked(position, TAG)
            }

            binding.nextButton.setOnClickListener {
                onClickListener.onAccountClicked(position, TAG)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListBankAccountsBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = listAccounts.size

    fun swapList(newList: List<WayaAccounts>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listAccounts, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        //update the list fo reviews
        listAccounts = newList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<WayaAccounts>,
                                   private val newList: List<WayaAccounts>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].accountNumber == newList[newItemPosition].accountNumber
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