package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListVoteListBinding
import com.wayapaychat.core.wayagram.Vote

class VoteListAdapter(private val postPosition: Int, private val voteClickListener: VoteClickListener): RecyclerView.Adapter<VoteListAdapter.MyViewHolder>() {

    private var listVote = listOf<Vote>()

    fun getSelectedVote(position: Int) = listVote[position]

    interface VoteClickListener{
        fun choiceSelected(position: Int, votePosition: Int)
    }

    fun setListVote(list: List<Vote>){
        listVote = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ListVoteListBinding): RecyclerView.ViewHolder(binding.root){

        fun binding(position: Int){
            val vote = getSelectedVote(position)

            binding.vote = vote

            binding.radioChoice.setOnClickListener {
                voteClickListener.choiceSelected(postPosition, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListVoteListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listVote.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.binding(position)
}