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
import com.wayapay.ng.landingpage.adapter.PagesAdapter
import com.wayapay.ng.landingpage.databinding.FragmentPagesBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.Tags

/**
 * A simple [Fragment] subclass.
 * Use the [PagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PagesFragment : Fragment(), PagesAdapter.PagesOnClick {

    private lateinit var binding: FragmentPagesBinding
    private lateinit var viewModel: GroupPagesViewModel
    private lateinit var myPagesAdapter: PagesAdapter
    private lateinit var followedPagesAdapter: PagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_pages, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(GroupPagesViewModel::class.java)

        myPagesAdapter = PagesAdapter(this, Tags.MY_PAGE)
        followedPagesAdapter = PagesAdapter(this, Tags.FOLLOWED_PAGE)

        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.myPagesRecyclerView.apply {
            layoutManager = manager
            adapter = myPagesAdapter
        }

        val fManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.pagesFollowRecyclerView.apply {
            layoutManager = fManager
            adapter = followedPagesAdapter
        }

        viewModel.getMyPageList().observe(requireActivity(), Observer {
            myPagesAdapter.swapList(it)
        })

        viewModel.getMyFollowedPages().observe(requireActivity(), Observer {
            followedPagesAdapter.swapList(it)
        })

        binding.viewModel = viewModel
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