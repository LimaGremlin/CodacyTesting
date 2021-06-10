package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListEventsBinding
import com.wayapay.ng.landingpage.models.WayaEvent

class EventAdapter(private val eventsClickListeners: EventsClickListeners,
                   private val TAG: String): RecyclerView.Adapter<EventAdapter.MyViewHolder>() {
    var listEvents = listOf<WayaEvent>()

    interface EventsClickListeners{
        fun onEventClicked(position: Int, tag: String)
        fun onInterestedClicked(position: Int, tag: String)
        fun onShareClicked(position: Int, tag: String)
        fun onMoreClicked(position: Int, tag: String)
        fun onImageClicked(position: Int, tag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListEventsBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listEvents.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    fun getEvent(position: Int):WayaEvent = listEvents[position]

    inner class MyViewHolder(private val binding: ListEventsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int){
            val event = getEvent(position)

            binding.root.setOnClickListener {
                eventsClickListeners.onEventClicked(position, TAG)
            }

            binding.event = event
        }
    }

    fun swapList(newList: List<WayaEvent>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listEvents, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list fo reviews
        listEvents = newList.toMutableList()

        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<WayaEvent>,
                                   private val newList: List<WayaEvent>) : DiffUtil.Callback() {

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