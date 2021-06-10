package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.wayapaychat.core.adapters.RecommendedAdapter
import com.wayapaychat.core.adapters.onUserInterestClicked
import com.wayapaychat.core.models.FollowInterestRequest
import com.wayapaychat.core.models.InterestSuggestionResponse
import com.wayapaychat.core.models.InterestSuggestionResponseData
import com.wayapaychat.core.models.UnfollowIntrestRequest
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentRecommendedBinding
import com.wayapaychat.ng.profile.presentation.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class   RecommendedFragment : Fragment(), View.OnClickListener, onUserInterestClicked {
    private lateinit var binding: FragmentRecommendedBinding

    private val viewModel: ProfileViewModel by navGraphViewModels(R.id.profile_nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recommended,
            container, false
        )

        setupListener()
        setupObservers()

        getData()

//        viewModel.getInterestSuggestions()

        setupListener()
        return binding.root
    }

    private fun setupObservers() {

//        viewModel.userInterestSuggestions.observe(viewLifecycleOwner, userInterestObserver)
    }

    private fun getData() {
        arguments?.let {
            val interest = it.get("interest")
        }
    }

    private fun setupListener() {
        binding.actionBtn.actionBtn.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecommendedFragment()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.actionBtn.actionBtn -> {
//                this.requireActivity().moveToAnother(LandingActivity())
            }
        }
    }

    private val userInterestObserver = Observer<InterestSuggestionResponse> {
        val adapter = RecommendedAdapter(this)
        adapter.submitList(it.data)
        binding.recommended.adapter = adapter
    }


    override fun onFollowClicked(interestData: InterestSuggestionResponseData) {
        val list = ArrayList<String>()
        list.add(interestData.id!!)
        val data = FollowInterestRequest(
            data = list
        )
//        viewModel.followInterest(data)
    }

    override fun onUnFollowClicked(interestData: InterestSuggestionResponseData) {
        val data = UnfollowIntrestRequest(
            id = interestData.id
        )
//        viewModel.unFollowInterest(data)
    }
}
