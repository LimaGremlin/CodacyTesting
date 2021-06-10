package com.wayapay.ng.landingpage.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.GramProfileActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.adapter.PostAdapter
import com.wayapay.ng.landingpage.databinding.FragmentSearchPostBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.WayaPost
import com.wayapaychat.core.wayagram.Vote
import com.wayapaychat.local.room.models.WayaGramUser

/**
 * A simple [Fragment] subclass.
 * Use the [SearchPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchPostFragment : Fragment(), PostAdapter.PostOnClickListener {

    private lateinit var binding: FragmentSearchPostBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search_post, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)

        postAdapter = PostAdapter(requireContext(), this, "")
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = postAdapter
        }

        viewModel.getListSearchedPost().observe(requireActivity(), Observer {
            postAdapter.swapList(it)
        })

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onPostClick(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onChoiceSelected(position: Int, vote: Vote, tag: String) {
        val post = postAdapter.getPost(position)
        //check if vote is free
        if(!post.poll.isPaid)freeVote(position, vote, tag)
    }

    private fun freeVote(position: Int, vote: Vote, tag: String){
        val post = postAdapter.getPost(position)
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()

        val map = hashMapOf(
            "post_id" to post.id,
            "profile_id" to user.id,
            "choice" to vote.option
        )
        viewModel.voteOnPost(map)

        //Increment vote count
        val sVote = Vote(vote.option, vote.cout + 1)
        //get list votes from post
        val votes = post.poll.listVotes
        post.poll.isVoted = true
        //Update vote in list post
        run breaker@{
            votes.forEachIndexed { index, v ->
                if (v.option == vote.option) {
                    votes.removeAt(index)
                    votes.add(index, sVote)
                    return@breaker
                }
            }
        }
        //update list vote in post
        post.poll.listVotes = votes
        //update post
        viewModel.updatePost(post)
    }

    override fun onPostImageClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onPostProfileClicked(position: Int, tag: String) {
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        val post = postAdapter.getPost(position)
        val profileOwner = post.user
        val intent = Intent(requireActivity(), GramProfileActivity::class.java)
        intent.putExtra(IntentBundles.PROFILE_KEY, profileOwner)
        intent.putExtra(IntentBundles.USER_ID_KEY, user.id)
        startActivity(intent)
    }

    override fun onPostOptionsClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onPostCommentClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onPostLiked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onPostRetweet(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onPostShare(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }
}