package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListGroupBinding
import com.wayapay.ng.landingpage.models.WayaGroup

class GroupAdapter(private val clickListener: GroupClickListener, private val TAG: String = "TAG"): RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {

    interface GroupClickListener{
        fun onGroupClicked(position: Int, tag: String)
        fun onGroupFollowClicked(position: Int, tag: String)
    }

    private var listGroup = listOf<WayaGroup>()

    fun getGroup(position: Int) = listGroup[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListGroupBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listGroup.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    inner class MyViewHolder(private val binding: ListGroupBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            val group = getGroup(position)
            binding.group = group

            binding.root.setOnClickListener {
                clickListener.onGroupClicked(position, TAG)
            }

            binding.button2.setOnClickListener {
                clickListener.onGroupFollowClicked(position, TAG)
            }
        }
    }

    fun swapList(newList: List<WayaGroup>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listGroup, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list fo reviews
        listGroup = newList.toMutableList()

        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<WayaGroup>,
                                   private val newList: List<WayaGroup>) : DiffUtil.Callback() {

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