package com.wayapaychat.ng.profile.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.core.models.LogInInfo
import com.wayapaychat.core.utils.Helpers.getDateWithServerTimeStamp
import com.wayapaychat.ng.profile.databinding.TimeLogItemBinding
import com.wayapaychat.ng.profile.presentation.model.auth.LoginHistory
import java.text.SimpleDateFormat
import java.util.*

class LoginHistoryAdapter: RecyclerView.Adapter<LoginHistoryAdapter.Holder>(){

    private var dataset = mutableListOf<LogInInfo>()

    fun populateDataset(dataset: List<LogInInfo>){
        this.dataset = dataset.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(TimeLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int =
        dataset.size

    inner class Holder(private val binding: TimeLogItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: LogInInfo){
            val date = item.logInDate.getDateWithServerTimeStamp()
            date?.let {
                val calendar = Calendar.getInstance()
                calendar.time = it
                binding.date.text = SimpleDateFormat("E. dd MMM. yyyy").format(it)
                binding.time.text = SimpleDateFormat("h:m a").format(it)
            }
            binding.location.text = "${item.province}, ${item.city}"
        }

    }

}