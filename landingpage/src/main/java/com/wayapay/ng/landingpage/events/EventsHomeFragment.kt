package com.wayapay.ng.landingpage.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentEventsHomeBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [EventsHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventsHomeFragment : Fragment() {

    private lateinit var binding: FragmentEventsHomeBinding
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_events_home, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(EventViewModel::class.java)

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }
}