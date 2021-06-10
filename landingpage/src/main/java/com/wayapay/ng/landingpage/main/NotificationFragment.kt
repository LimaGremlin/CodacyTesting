package com.wayapay.ng.landingpage.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentNotificationBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var viewModel: LandingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_notification, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LandingViewModel::class.java)

        viewModel.setAddPeopleVisibility(true)
        viewModel.setOptionsVisibility(false)
        viewModel.setDrawerEnabled(true)
        viewModel.setIsBottomNavVisible(true)
        viewModel.setIsHeaderVisible(true)
        viewModel.setFabVisibility(true)
        viewModel.setSearchVisibility(false)
        viewModel.setSearchButtonVisibility(false)
        viewModel.setHeaderText(getString(R.string.notifications))

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }
}