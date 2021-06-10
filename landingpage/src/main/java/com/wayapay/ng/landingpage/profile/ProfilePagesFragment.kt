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
import com.wayapay.ng.landingpage.adapter.PagesAdapter
import com.wayapay.ng.landingpage.databinding.FragmentProfilePagesBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilePagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilePagesFragment : Fragment(), PagesAdapter.PagesOnClick {

    private lateinit var binding: FragmentProfilePagesBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var pageAdapter: PagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile_pages, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ProfileViewModel::class.java)

        pageAdapter = PagesAdapter(this)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = pageAdapter
        }

        viewModel.getListPages().observe(requireActivity(), Observer {
            pageAdapter.swapList(it)
        })

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onPageClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }

    override fun onPageFollowButtonClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
    }
}