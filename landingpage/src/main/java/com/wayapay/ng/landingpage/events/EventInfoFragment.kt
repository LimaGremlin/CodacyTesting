package com.wayapay.ng.landingpage.events

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapay.ng.landingpage.NewEventActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentEventInfoBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.Tags
import com.wayapay.ng.landingpage.models.WayaEvent

/**
 * A simple [Fragment] subclass.
 * Use the [EventInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventInfoFragment : Fragment() {

    private lateinit var binding: FragmentEventInfoBinding
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_event_info, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(EventViewModel::class.java)

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewEventActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val event = viewModel.getWayaEvent().value ?: WayaEvent()
            if(check(event))findNavController().navigate(R.id.action_eventInfoFragment_to_eventLocationFragment)

        }

        val event = viewModel.getWayaEvent().value ?: WayaEvent()
        if(event.tag == Tags.EDIT)setValues(event)

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun setValues(event: WayaEvent){
        if(event.private){
            binding.radioPrivate.isChecked = true
            binding.radioPublic.isChecked = false
        }else{
            binding.radioPrivate.isChecked = false
            binding.radioPublic.isChecked = true
        }
    }

    private fun check(event: WayaEvent):Boolean{
        if(TextUtils.isEmpty(binding.eventTitle.text.toString())){
            binding.eventTitle.error = ""
            binding.eventTitle.requestFocus()
            return false
        }else event.title = binding.eventTitle.text.toString()

        if(TextUtils.isEmpty(binding.organizersName.text.toString())){
            binding.organizersName.error = ""
            binding.organizersName.requestFocus()
            return false
        }else event.organizer = binding.organizersName.text.toString()

        //ToDo Check for list of tags

        if((!binding.radioPrivate.isChecked) && (!binding.radioPublic.isChecked)){
            binding.privateMessage.error = ""
            binding.radioPublic.requestFocus()
            return false
        }else event.private = binding.radioPrivate.isChecked

        viewModel.setWayaEvent(event)

        return true
    }
}