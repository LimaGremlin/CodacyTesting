package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListVoteStatBinding
import com.wayapay.ng.landingpage.utils.getVotePercent
import com.wayapaychat.core.wayagram.Vote

class VoteStatAdapter() : RecyclerView.Adapter<VoteStatAdapter.MyViewHolder>() {

    private var listVotes = listOf<Vote>()

    fun getListVote(position: Int):Vote = listVotes[position]

    fun setListVotes(list: List<Vote>){
        listVotes = list
        notifyDataSetChanged()
    }

    fun getListVotesCount():Int = listVotes.map { it.cout }.sum()

    inner class MyViewHolder(private var binding: ListVoteStatBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            val vote = getListVote(position)

            binding.percent.text = getVotePercent(vote.cout, getListVotesCount())
            binding.progressBar.max = getListVotesCount()
            binding.progressBar.min = 0
            binding.progressBar.progress = vote.cout

            binding.progressBar.isEnabled = false

            binding.vote = vote
            binding.maxVotes = getListVotesCount()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListVoteStatBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listVotes.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)
}