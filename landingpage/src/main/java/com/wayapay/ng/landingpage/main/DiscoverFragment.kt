package com.wayapay.ng.landingpage.main

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
import com.wayapay.ng.landingpage.PageActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.adapter.*
import com.wayapay.ng.landingpage.databinding.FragmentDiscoverBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.WayaPost
import com.wayapaychat.core.wayagram.Vote
import com.wayapaychat.local.room.models.WayaGramUser

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment : Fragment(), PeopleAdapter.PeopleAdapterOnClickListener, GroupAdapter.GroupClickListener, PagesAdapter.PagesOnClick, PostAdapter.PostOnClickListener {

    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var viewModel: LandingViewModel

    private lateinit var personAdapter: PeopleAdapter
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var interestAdapter: InterestAdapter
    private lateinit var pagesAdapter: PagesAdapter
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_discover, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LandingViewModel::class.java)

        viewModel.setAddPeopleVisibility(true)
        viewModel.setOptionsVisibility(false)
        viewModel.setDrawerEnabled(true)
        viewModel.setIsBottomNavVisible(true)
        viewModel.setIsHeaderVisible(true)
        viewModel.setFabVisibility(true)
        viewModel.setSearchVisibility(true)
        viewModel.setSearchButtonVisibility(false)

        personAdapter = PeopleAdapter(this)
        groupAdapter = GroupAdapter(this)
        pagesAdapter = PagesAdapter(this)
        postAdapter = PostAdapter(requireContext(), this, "")

        val personManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val groupManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val pageManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val postManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.peopleRecyclerView.apply {
            layoutManager = personManager
            adapter = personAdapter
        }

        binding.groupsRecyclerView.apply {
            layoutManager = groupManager
            adapter = groupAdapter
        }

        binding.pagesRecyclerview.apply {
            layoutManager = pageManager
            adapter = pagesAdapter
        }

        binding.postRecyclerView.apply {
            layoutManager = postManager
            adapter = postAdapter
        }

        viewModel.getListSearchedUsers().observe(requireActivity(), Observer {
            val list = it.shuffled()
            personAdapter.swapList(list.take(30))
        })

        viewModel.getListSearchedGroups().observe(requireActivity(), Observer {
            val list = it.shuffled()
            groupAdapter.swapList(list.take(5))
        })

        viewModel.getListSearchedPages().observe(requireActivity(), Observer {
            val list = it.shuffled()
            pagesAdapter.swapList(list.take(5))
        })

        viewModel.getListSearchedPost().observe(requireActivity(), Observer {
            val list = it.shuffled()
            postAdapter.swapList(list.take(50))
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onProfileClicked(position: Int) {
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        val profileOwner = personAdapter.getPeople(position)
        val intent = Intent(requireActivity(), GramProfileActivity::class.java)
        intent.putExtra(IntentBundles.PROFILE_KEY, profileOwner)
        intent.putExtra(IntentBundles.USER_ID_KEY, user.id)
        startActivity(intent)
    }

    override fun onFollowClicked(position: Int) {
        //TODO("Not yet implemented")
    }

    override fun onGroupClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onGroupFollowClicked(position: Int, tag: String) {
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
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        val page = pagesAdapter.getPage(position)
        page.isFollowing = !page.isFollowing
        viewModel.followPageAsync(page.pageId, user.id)
        pagesAdapter.updateItem(page, position)
        viewModel.updateListSearchedPages(page)
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