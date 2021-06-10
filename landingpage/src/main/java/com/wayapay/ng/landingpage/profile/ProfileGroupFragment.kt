package com.wayapay.ng.landingpage.profile

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
import com.wayapay.ng.landingpage.databinding.FragmentProfileGroupBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileGroupFragment : Fragment(), GroupAdapter.GroupClickListener {

    private lateinit var binding: FragmentProfileGroupBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile_group, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ProfileViewModel::class.java)

        groupAdapter = GroupAdapter(this)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = groupAdapter
        }

        viewModel.getMyGroupList().observe(requireActivity(), Observer {
            groupAdapter.swapList(it)
        })

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onGroupClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onGroupFollowClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }
}