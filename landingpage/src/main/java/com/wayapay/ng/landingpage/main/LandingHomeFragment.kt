package com.wayapay.ng.landingpage.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.*
import com.wayapay.ng.landingpage.adapter.EventAdapter
import com.wayapay.ng.landingpage.adapter.PagesAdapter
import com.wayapay.ng.landingpage.adapter.PostAdapter
import com.wayapay.ng.landingpage.databinding.FragmentLandingHomeBinding
import com.wayapay.ng.landingpage.models.*
import com.wayapaychat.core.wayagram.Vote
import com.wayapaychat.local.room.models.WayaGramUser

/**
 * A simple [Fragment] subclass.
 * Use the [LandingHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingHomeFragment : Fragment(), EventAdapter.EventsClickListeners,
    PostAdapter.PostOnClickListener, PagesAdapter.PagesOnClick {

    private lateinit var viewModel: LandingViewModel
    private lateinit var binding: FragmentLandingHomeBinding
    private lateinit var eventAdapter: EventAdapter
    private lateinit var postAdapter: PostAdapter
    private lateinit var pagesAdapter: PagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_landing_home, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LandingViewModel::class.java)

        val sharedPref = requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val hasViewedInterest = sharedPref.getBoolean(getString(R.string.has_viewed_interest_key), false)
        if(!hasViewedInterest)findNavController().navigate(R.id.action_landingHomeFragment_to_interestFragment)

        eventAdapter = EventAdapter(this, Tags.EVENTS)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.eventRecyclerView.apply {
            layoutManager = manager
            adapter = eventAdapter
        }
        viewModel.getListAllEvents().observe(requireActivity(), Observer {
            eventAdapter.swapList(it.shuffled().take(10))
        })

        pagesAdapter = PagesAdapter(this)
        val pagesManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.pagesRecyclerview.apply {
            layoutManager = pagesManager
            adapter = pagesAdapter
        }
        viewModel.getListSearchedPages().observe(requireActivity(), Observer {
            pagesAdapter.swapList(it.shuffled().takeLast(3))
        })

        postAdapter = PostAdapter(requireContext(), this, Tags.POST_MAIN)
        val postManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.postRecyclerView.apply {
            layoutManager = postManager
            adapter = postAdapter
        }
        viewModel.getListPost().observe(requireActivity(), Observer {
            postAdapter.swapList(it)
        })

        viewModel.setAddPeopleVisibility(true)
        viewModel.setOptionsVisibility(false)
        viewModel.setDrawerEnabled(true)
        viewModel.setIsBackEnabled(false)
        viewModel.setIsBottomNavVisible(true)
        viewModel.setIsHeaderVisible(true)
        viewModel.setFabVisibility(true)
        viewModel.setSearchVisibility(false)
        viewModel.setSearchButtonVisibility(false)
        viewModel.setHeaderText(getString(R.string.wayagram))

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onEventClicked(position: Int, tag: String) {
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        val event = eventAdapter.getEvent(position)
        event.isOwner = user.id == event.creatorId
        val intent = Intent(requireActivity(), EventsActivity::class.java)
        intent.putExtra(IntentBundles.EVENT_KEY, event)
        startActivity(intent)
    }

    override fun onInterestedClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onShareClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onMoreClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onImageClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onPostClick(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onChoiceSelected(position: Int, vote: Vote, tag: String) {
        val post = postAdapter.getPost(position)
        viewModel.setSelectedPost(post)
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

    override fun onPageClicked(position: Int, tag: String) {
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        val page = pagesAdapter.getPage(position)
        val intent = Intent(requireActivity(), PageActivity::class.java)
        intent.putExtra(IntentBundles.PAGES_KEY, page)
        intent.putExtra(IntentBundles.GRAM_PROFILE_KEY, user)
        startActivity(intent)
    }

    override fun onPageFollowButtonClicked(position: Int, tag: String) {
        val page = pagesAdapter.getPage(position)
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        //Follow a page
        viewModel.followPageAsync(page.pageId, user.id)
    }

    override fun onResume() {
        super.onResume()

    }
}