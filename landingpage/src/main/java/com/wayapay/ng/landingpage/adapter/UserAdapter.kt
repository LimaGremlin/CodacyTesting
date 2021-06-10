package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListUsersBinding
import com.wayapaychat.local.room.models.WayaGramUser

class UserAdapter(private val onClickListener: OnClickListener, private val TAG: String = "TAG"): RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    interface OnClickListener{
        fun onProfileClicked(position: Int, tag: String)
    }

    private var listUser = mutableListOf<WayaGramUser>()
    fun getUser(position: Int) = listUser[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListUsersBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.onBind(position)

    override fun getItemCount(): Int = listUser.size

    inner class MyViewHolder(private val binding: ListUsersBinding): RecyclerView.ViewHolder(binding.root){

        fun onBind(position: Int){
            val user = getUser(position)
            binding.user = user

            binding.imageButton12.setOnClickListener {
                onClickListener.onProfileClicked(position, TAG)
            }

            binding.root.setOnClickListener {
                onClickListener.onProfileClicked(position, TAG)
            }
        }
    }

    fun swapList(newList: List<WayaGramUser>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listUser, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list fo reviews
        listUser = newList.toMutableList()

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