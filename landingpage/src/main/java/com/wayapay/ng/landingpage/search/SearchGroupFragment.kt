package com.wayapay.ng.landingpage.search

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
import com.wayapay.ng.landingpage.databinding.FragmentSearchGroupBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [SearchGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchGroupFragment : Fragment(), GroupAdapter.GroupClickListener {

    private lateinit var binding: FragmentSearchGroupBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search_group, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)

        groupAdapter = GroupAdapter(this)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = groupAdapter
        }

        viewModel.getListSearchedGroups().observe(requireActivity(), Observer {
            groupAdapter.swapList(it)
        })

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onGroupClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onGroupFollowClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }
}