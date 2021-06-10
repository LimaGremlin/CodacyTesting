package com.wayapay.ng.landingpage.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentViewEventBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ViewEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewEventFragment : Fragment() {

    private lateinit var binding: FragmentViewEventBinding
    private lateinit var viewModel: LandingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_view_event, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LandingViewModel::class.java)

        viewModel.setAddPeopleVisibility(false)
        viewModel.setOptionsVisibility(true)
        viewModel.setDrawerEnabled(false)
        viewModel.setIsBackEnabled(true)
        viewModel.setIsBottomNavVisible(false)
        viewModel.setIsHeaderVisible(true)
        viewModel.setFabVisibility(false)
        viewModel.setSearchButtonVisibility(false)
        viewModel.setHeaderText(getString(R.string.event))

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }
}