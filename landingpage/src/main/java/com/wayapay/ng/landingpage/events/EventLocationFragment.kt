package com.wayapay.ng.landingpage.events

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapay.ng.landingpage.NewEventActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentEventLocationBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.WayaEvent

/**
 * A simple [Fragment] subclass.
 * Use the [EventLocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventLocationFragment : Fragment() {

    private lateinit var binding: FragmentEventLocationBinding
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_event_location, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(EventViewModel::class.java)

        binding.virtualButton.setOnClickListener { viewModel.setIsVirtualEvent(true) }

        binding.physicalButton.setOnClickListener { viewModel.setIsVirtualEvent(false) }

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewEventActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val event = viewModel.getWayaEvent().value ?: WayaEvent()
            if(check(event))findNavController().navigate(R.id.action_eventLocationFragment_to_eventDayTimeFragment)

        }

        setSelectedButton(viewModel.getIsVirtualEvent().value ?: false)
        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun setSelectedButton(isVirtual: Boolean){
        if(isVirtual)binding.toggleButton.check(R.id.virtual_button)
        else binding.toggleButton.check(R.id.physical_button)
    }

    private fun check(event: WayaEvent):Boolean{

        val isVirtual = viewModel.getIsVirtualEvent().value ?: false
        event.virtual = isVirtual

        if(TextUtils.isEmpty(binding.eventLocation.text.toString())){
            binding.eventLocation.error = ""
            binding.eventLocation.requestFocus()
            return false
        }else event.eventLocation = binding.eventLocation.text.toString()

        event.urlLink = binding.eventLink.text.toString()

        viewModel.setWayaEvent(event)

        return true
    }
}