package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListStringBinding

class StringAdapter(private val listOnClickListener: ListOnClickListener):
    RecyclerView.Adapter<StringAdapter.MyViewHolder>() {

    var listString = listOf<String>()

    fun getItem(position: Int):String = listString[position]

    interface ListOnClickListener{
        fun itemClicked(position: Int)
    }

    fun setList(list:List<String>){
        listString = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ListStringBinding):RecyclerView.ViewHolder(binding.root){
        fun check(position:Int){
            val tag = getItem(position)
            binding.tag = tag

            binding.root.setOnClickListener {
                listOnClickListener.itemClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListStringBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listString.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.check(position)
}