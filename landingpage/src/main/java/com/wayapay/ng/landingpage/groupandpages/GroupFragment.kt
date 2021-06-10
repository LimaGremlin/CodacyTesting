package com.wayapay.ng.landingpage.groupandpages

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
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.adapter.GroupAdapter
import com.wayapay.ng.landingpage.databinding.FragmentGroupBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.Tags

/**
 * A simple [Fragment] subclass.
 * Use the [GroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupFragment : Fragment(), GroupAdapter.GroupClickListener {

    private lateinit var viewModel: GroupPagesViewModel
    private lateinit var binding: FragmentGroupBinding
    private lateinit var myGroupAdapter: GroupAdapter
    private lateinit var followedGroupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_group, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(GroupPagesViewModel::class.java)

        myGroupAdapter = GroupAdapter(this, Tags.MY_GROUP)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.myGroupsRecyclerView.apply {
            layoutManager = manager
            adapter = myGroupAdapter
        }

        followedGroupAdapter = GroupAdapter(this, Tags.JOINED_GROUP)
        val jManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.groupsImInRecyclerView.apply {
            layoutManager = jManager
            adapter = followedGroupAdapter
        }

        viewModel.getMyGroupList().observe(requireActivity(), Observer {
            myGroupAdapter.swapList(it)
        })

        viewModel.getMyJoinedGroupList().observe(requireActivity(), Observer {
            followedGroupAdapter.swapList(it)
        })

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onGroupClicked(position: Int, tag: String) {
        TODO("Not yet implemented")
    }

    override fun onGroupFollowClicked(position: Int, tag: String) {
        TODO("Not yet implemented")
    }
}