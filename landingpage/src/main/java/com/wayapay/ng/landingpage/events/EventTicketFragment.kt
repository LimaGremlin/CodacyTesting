package com.wayapay.ng.landingpage.events

import android.os.Bundle
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
import com.wayapay.ng.landingpage.databinding.FragmentEventTicketBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.Tags
import com.wayapay.ng.landingpage.models.WayaEvent
import com.wayapay.ng.landingpage.utils.toDouble

/**
 * A simple [Fragment] subclass.
 * Use the [EventTicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventTicketFragment : Fragment() {

    private lateinit var binding: FragmentEventTicketBinding
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_event_ticket, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(EventViewModel::class.java)

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewEventActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val event = viewModel.getWayaEvent().value ?: WayaEvent()
            if(check(event)){
                findNavController().navigate(R.id.action_eventTicketFragment_to_publishEventFragment)
                viewModel.setButtonText(getString(R.string.publish))
            }

        }

        val event = viewModel.getWayaEvent().value ?: WayaEvent()
        if(event.tag == Tags.EDIT)setValues(event)

        binding.radioFree.setOnClickListener { onRadioButtonClicked(it) }

        binding.radioPaid.setOnClickListener { onRadioButtonClicked(it) }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun setValues(event: WayaEvent){
        if(event.freeToAttend){
            binding.radioFree.isChecked = true
            binding.radioPaid.isChecked = false
            binding.feeLayout.visibility = View.GONE
        }else {
            binding.radioFree.isChecked = false
            binding.radioPaid.isChecked = true
            binding.feeLayout.visibility = View.VISIBLE
            binding.ticketEditText.setText(event.fee.toString())
        }
    }

    private fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_paid ->
                    if (checked) {
                        // Pirates are the best
                        binding.feeLayout.visibility = View.VISIBLE
                    }
                R.id.radio_free ->
                    if (checked) {
                        binding.feeLayout.visibility = View.GONE
                    }
            }
        }
    }

    private fun check(event: WayaEvent):Boolean{
        if((!binding.radioPaid.isChecked) && (!binding.radioFree.isChecked)){
            binding.textView14.error = ""
            binding.radioFree.requestFocus()
            return false
        }
        if(binding.radioFree.isChecked){
            event.freeToAttend = true
            event.fee = 0.00
        }
        if(binding.radioPaid.isChecked){
            event.freeToAttend = false
            event.fee = binding.ticketEditText.toDouble()
        }

        viewModel.setWayaEvent(event)

        return true
    }
}