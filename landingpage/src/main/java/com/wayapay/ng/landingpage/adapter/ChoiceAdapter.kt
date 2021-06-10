package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListChoiceBinding
import com.wayapaychat.core.wayagram.Vote

/***
 * Choice Adapter is for Vote Post Creation
 */
class ChoiceAdapter: RecyclerView.Adapter<ChoiceAdapter.MyViewHolder>() {

    var listEditText = mutableListOf<Vote>()

    fun getEditText(position: Int) = listEditText[position]

    fun setList(list: List<Vote>){
        listEditText = list.toMutableList()
        notifyDataSetChanged()
    }

    fun addChoice(choice: Vote){
        listEditText.add(choice)
        notifyItemInserted(listEditText.size)
    }

    fun removeChoice(){
        listEditText.removeLast()
        notifyItemRemoved(listEditText.size)
    }

    fun getListChoices(): List<Vote> = listEditText

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListChoiceBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listEditText.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    inner class MyViewHolder(private var binding: ListChoiceBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val item = getEditText(position)
            binding.choiceNumber.text = "${(position + 1)}."
            binding.choice = item
        }
    }


}