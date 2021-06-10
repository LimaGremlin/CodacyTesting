package com.wayapay.ng.landingpage.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListPostBinding
import com.wayapay.ng.landingpage.models.PostType
import com.wayapay.ng.landingpage.models.WayaPost
import com.wayapaychat.core.wayagram.Vote

class PostAdapter(private val context: Context, private val postOnClickListener: PostOnClickListener,
                  private val TAG:String): RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    var listPost = mutableListOf<WayaPost>()

    fun getPost(position: Int) = listPost[position]

    interface PostOnClickListener{
        fun onPostClick(position: Int, tag:String)
        fun onChoiceSelected(position: Int, vote: Vote, tag: String)
        fun onPostImageClicked(position: Int, tag: String)
        fun onPostProfileClicked(position: Int, tag: String)
        fun onPostOptionsClicked(position: Int, tag: String)
        fun onPostCommentClicked(position: Int, tag: String)
        fun onPostLiked(position: Int, tag: String)
        fun onPostRetweet(position: Int, tag: String)
        fun onPostShare(position: Int, tag: String)
    }

    inner class MyViewHolder(private val binding: ListPostBinding): RecyclerView.ViewHolder(binding.root),
        VoteListAdapter.VoteClickListener, ImagesAdapter.OnImageAdapterClickListener {

        private lateinit var voteListAdapter: VoteListAdapter
        private lateinit var statListAdapter: VoteStatAdapter
        private lateinit var imageAdapter: ImagesAdapter

        fun bind(position: Int){
            val post = getPost(position)
            Log.d("search_post", post.listImageUrl.toString())

            voteListAdapter = VoteListAdapter(position, this)
            statListAdapter = VoteStatAdapter()
            imageAdapter = ImagesAdapter(this)

            if(post.isPoll){
                //Display relivant views
                binding.voteLayout.postParentLayout.visibility = View.VISIBLE
                binding.imagesRecyclerView.visibility = View.GONE
                binding.productLayout.visibility = View.GONE

                //Check is user has voted on a post or not
                if(post.poll.isVoted){
                    binding.voteLayout.resultRecyclerview.visibility = View.VISIBLE
                    binding.voteLayout.optionsRecyclerview.visibility = View.GONE
                    val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.voteLayout.resultRecyclerview.apply {
                        layoutManager = manager
                        adapter = statListAdapter
                    }
                    statListAdapter.setListVotes(post.poll.listVotes)
                }else{
                    binding.voteLayout.resultRecyclerview.visibility = View.GONE
                    binding.voteLayout.optionsRecyclerview.visibility = View.VISIBLE
                    //Adapter to display list of choices
                    val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.voteLayout.optionsRecyclerview.apply {
                        layoutManager = manager
                        adapter = voteListAdapter
                    }
                    //Set list of choices in adapter
                    voteListAdapter.setListVote(post.poll.listVotes)
                }
            }else{

                binding.voteLayout.postParentLayout.visibility = View.GONE
                binding.imagesRecyclerView.visibility = View.VISIBLE
                binding.productLayout.visibility = View.GONE
                if(post.type == PostType.PRODUCT)
                    binding.productLayout.visibility = View.VISIBLE

                //Set up ImageRecyclerview with image list
                val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                binding.imagesRecyclerView.apply {
                    layoutManager = manager
                    adapter = imageAdapter
                }

                //Set list of user images in ImagesAdapter
                imageAdapter.setList(post.listImageUrl)
            }


            //On Post click listener
            binding.root.setOnClickListener {
                postOnClickListener.onPostClick(position, TAG)
            }
            binding.profileImage.setOnClickListener {
                postOnClickListener.onPostProfileClicked(position, TAG)
            }
            binding.userName.setOnClickListener {
                postOnClickListener.onPostProfileClicked(position, TAG)
            }
            binding.displayName.setOnClickListener {
                postOnClickListener.onPostProfileClicked(position, TAG)
            }
            binding.optionsButton.setOnClickListener {
                postOnClickListener.onPostOptionsClicked(position, TAG)
            }
            binding.commentButton.setOnClickListener {
                postOnClickListener.onPostCommentClicked(position, TAG)
            }
            binding.likeButton.setOnClickListener {
                postOnClickListener.onPostLiked(position, TAG)
            }
            binding.retweetButton.setOnClickListener {
                postOnClickListener.onPostRetweet(position, TAG)
            }
            binding.shareButton.setOnClickListener {
                postOnClickListener.onPostShare(position, TAG)
            }

            binding.post = post
        }

        override fun choiceSelected(position: Int, votePosition: Int) {
            val choice = voteListAdapter.getSelectedVote(votePosition)
            postOnClickListener.onChoiceSelected(position, choice, TAG)
        }

        override fun onImageClicked(position: Int) {
            //TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListPostBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listPost.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    fun swapList(newList: List<WayaPost>){
        //Check difference between old and new list then get result
        val diffCallBack = ItemDiffCallBack(listPost, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        //update the list fo reviews
        listPost = newList.toMutableList()

        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    private class ItemDiffCallBack(private val oldList: List<WayaPost>,
                                   private val newList: List<WayaPost>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].updatedAt == newList[newItemPosition].updatedAt
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