package com.wayapaychat.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wayapaychat.core.R
import com.wayapaychat.core.databinding.RecommendedListItemBinding
import com.wayapaychat.core.models.InterestSuggestionResponseData

class RecommendedAdapter(val listener: onUserInterestClicked) :
    ListAdapter<InterestSuggestionResponseData, RecommendedViewHolder>(RecommendedDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        return RecommendedViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class RecommendedViewHolder(val binding: RecommendedListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(interest: InterestSuggestionResponseData, listener: onUserInterestClicked) {
        binding.textName.text = interest.title
        binding.textUsername.text = interest.description
        binding.btnFollow.setOnClickListener {

            when (binding.btnFollow.text) {
                "Follow" -> {
                    binding.btnFollow.text = "Following"
                    binding.btnFollow.setBackgroundColor(
                        getColor(
                            binding.root.context,
                            R.color.colorPrimary
                        )
                    )
                    listener.onFollowClicked(interest)
                }

                "Following" -> {
                    binding.btnFollow.text = "Follow"
                    binding.btnFollow.setBackgroundColor(
                        getColor(
                            binding.root.context,
                            R.color.white
                        )
                    )
                    listener.onUnFollowClicked(interest)

                }
            }
        }
        Glide.with(binding.root).load(R.drawable.profile_photo_placeholder)
            .placeholder(R.drawable.profile_photo_placeholder)
            .into(binding.profilePhotoImage)
    }


    companion object {
        fun from(parent: ViewGroup): RecommendedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RecommendedListItemBinding.inflate(layoutInflater, parent, false)
            return RecommendedViewHolder(binding)
        }
    }
}

class RecommendedDiffCallback : DiffUtil.ItemCallback<InterestSuggestionResponseData>() {
    override fun areItemsTheSame(
        oldItem: InterestSuggestionResponseData,
        newItem: InterestSuggestionResponseData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: InterestSuggestionResponseData,
        newItem: InterestSuggestionResponseData
    ): Boolean {
        return oldItem == newItem
    }
}

interface onUserInterestClicked {
    fun onFollowClicked(interestData: InterestSuggestionResponseData)
    fun onUnFollowClicked(interestData: InterestSuggestionResponseData)
}
