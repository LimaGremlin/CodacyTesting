package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListPeopleBinding
import com.wayapaychat.local.room.models.WayaGramUser

class PeopleAdapter(private val onClick: PeopleAdapterOnClickListener, private val TAG:String = "TAG"): RecyclerView.Adapter<PeopleAdapter.MyViewHolder>(){

    interface PeopleAdapterOnClickListener{
        fun onProfileClicked(position: Int)
        fun onFollowClicked(position: Int)
    }

    private var listPeople = listOf<WayaGramUser>()

    fun getPeople(position: Int) = listPeople[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListPeopleBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listPeople.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    inner class MyViewHolder(private val binding: ListPeopleBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            val user = getPeople(position)

            binding.user = user
            binding.button3.setOnClickListener {
                onClick.onFollowClicked(position)
            }
            binding.profileImage.setOnClickListener {
                onClick.onProfileClicked(position)
            }
            binding.root.setOnClickListener {
                onClick.onProfileClicked(position)
            }
        }
    }

    fun swapList(newList: List<WayaGramUser>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listPeople, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list fo reviews
        listPeople = newList.toMutableList()

        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<WayaGramUser>,
                                   private val newList: List<WayaGramUser>) : DiffUtil.Callback() {

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