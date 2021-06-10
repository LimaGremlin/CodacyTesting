package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListPagesBinding
import com.wayapay.ng.landingpage.models.WayaPages

class PagesAdapter(private val clickListener: PagesOnClick, private val TAG: String = "TAG"):RecyclerView.Adapter<PagesAdapter.MyViewHolder>() {

    interface PagesOnClick{
        fun onPageClicked(position: Int, tag: String)
        fun onPageFollowButtonClicked(position: Int, tag: String)
    }

    var listPages = mutableListOf<WayaPages>()

    fun getPage(position: Int) = listPages[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListPagesBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listPages.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    fun updateItem(item: WayaPages, position: Int){
        listPages.removeAt(position)
        listPages.add(position, item)
        notifyItemChanged(position)
    }

    inner class MyViewHolder(private val binding: ListPagesBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            val page = getPage(position)

            binding.button2.setOnClickListener {
                clickListener.onPageFollowButtonClicked(position, TAG)
            }

            binding.root.setOnClickListener {
                clickListener.onPageClicked(position, TAG)
            }

            binding.page = page
        }
    }

    fun swapList(newList: List<WayaPages>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listPages, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list fo reviews
        listPages = newList.toMutableList()

        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<WayaPages>,
                                   private val newList: List<WayaPages>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].pageId == newList[newItemPosition].pageId
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