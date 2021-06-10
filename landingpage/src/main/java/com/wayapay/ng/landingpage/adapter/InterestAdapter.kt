package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListInterestBinding
import com.wayapay.ng.landingpage.models.WayaInterest

class InterestAdapter(private val onClickListener: InterestOnClickListener, private val TAG:String = "TAG"): RecyclerView.Adapter<InterestAdapter.MyViewHolder>() {

    interface InterestOnClickListener{
        fun onInterestClicked(position: Int)
    }

    private var listInterest = listOf<WayaInterest>()

    fun getInterest(position: Int) = listInterest[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListInterestBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listInterest.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.binding(position)

    inner class MyViewHolder(private val binding: ListInterestBinding): RecyclerView.ViewHolder(binding.root){

        fun binding(position: Int){
            val interest = getInterest(position)

            binding.interest = interest

            binding.root.setOnClickListener {
                onClickListener.onInterestClicked(position)
            }
        }
    }

    fun swapList(newList: List<WayaInterest>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listInterest, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list fo reviews
        listInterest = newList.toMutableList()

        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<WayaInterest>,
                                   private val newList: List<WayaInterest>) : DiffUtil.Callback() {

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