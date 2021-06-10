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
import com.wayapay.ng.landingpage.databinding.FragmentEventDayTimeBinding
import com.wayapay.ng.landingpage.dialog.DatePickerDialog
import com.wayapay.ng.landingpage.dialog.TimePickerFragment
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.Tags
import com.wayapay.ng.landingpage.models.WayaEvent
import com.wayapay.ng.landingpage.utils.getNumberDate
import com.wayapay.ng.landingpage.utils.getTime
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [EventDayTimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventDayTimeFragment : Fragment(), DatePickerDialog.OnClickListener,
    TimePickerFragment.OnClickListener {

    private lateinit var binding: FragmentEventDayTimeBinding
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_event_day_time, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(EventViewModel::class.java)

        binding.startDate.setOnClickListener {
            val newFragment = DatePickerDialog(requireActivity(), this, Tags.START_DATE)
            newFragment.show(requireActivity().supportFragmentManager, "datePicker")
        }

        binding.startTime.setOnClickListener {
            val newFragment = TimePickerFragment(requireActivity(), this, Tags.START_TIME)
            newFragment.show(requireActivity().supportFragmentManager, "datePicker")
        }

        binding.endDate.setOnClickListener {
            val newFragment = DatePickerDialog(requireActivity(), this, Tags.END_DATE)
            newFragment.show(requireActivity().supportFragmentManager, "datePicker")
        }

        binding.endTime.setOnClickListener {
            val newFragment = TimePickerFragment(requireActivity(), this, Tags.END_TIME)
            newFragment.show(requireActivity().supportFragmentManager, "datePicker")
        }

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewEventActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val event = viewModel.getWayaEvent().value ?: WayaEvent()
            if(check(event))findNavController().navigate(R.id.action_eventDayTimeFragment_to_eventTicketFragment)

        }

        val event = viewModel.getWayaEvent().value ?: WayaEvent()
        if(event.tag == Tags.EDIT)setValues(event)

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun setValues(event: WayaEvent){
        binding.startTime.setText(getTime(event.startTime))
        binding.endTime.setText(getTime(event.endTime))
        binding.startDate.setText(getNumberDate(event.startTime))
        binding.endDate.setText(getNumberDate(event.endTime))
    }

    override fun onDatePicked(date: GregorianCalendar, tag:String) {
        val ts = date.timeInMillis
        when(tag){
            Tags.START_DATE -> {
                binding.startDate.setText(getNumberDate(ts))
                viewModel.setEventStartTime(ts)
            }
            Tags.END_DATE -> {
                binding.endDate.setText(getNumberDate(ts))
                viewModel.setEventEndTime(ts)
            }
        }
    }

    override fun onTimePicked(hourOfDay: Int, minute: Int, tag:String) {

        when(tag){
            Tags.START_TIME -> {
                val sTime = viewModel.getEventStartTime().value ?: System.currentTimeMillis()
                val date = GregorianCalendar()
                //set time to selected time
                date.timeInMillis = sTime
                date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                date.set(Calendar.MINUTE, minute)
                //set selected time in edit text
                binding.startTime.setText(getTime(date.timeInMillis))
                //set selected time in view model
                viewModel.setEventStartTime(date.timeInMillis)
            }
            Tags.END_TIME -> {
                val eTime = viewModel.getEventEndTime().value ?: System.currentTimeMillis()
                val date = GregorianCalendar()
                //set time to selected time
                date.timeInMillis = eTime
                date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                date.set(Calendar.MINUTE, minute)
                //set selected time in edit text
                binding.endTime.setText(getTime(date.timeInMillis))
                //set selected time in view model
                viewModel.setEventEndTime(date.timeInMillis)
            }
        }
    }

    private fun check(event: WayaEvent):Boolean{
        if(TextUtils.isEmpty(binding.startDate.text.toString())){
            binding.startDate.error = ""
            binding.startDate.requestFocus()
            return false
        }
        if(TextUtils.isEmpty(binding.startTime.text.toString())){
            binding.startTime.error = ""
            binding.startTime.requestFocus()
            return false
        }
        if(TextUtils.isEmpty(binding.endDate.text.toString())){
            binding.endDate.error = ""
            binding.endDate.requestFocus()
            return false
        }
        if(TextUtils.isEmpty(binding.endTime.text.toString())){
            binding.endTime.error = ""
            binding.endTime.requestFocus()
            return false
        }

        event.startTime = viewModel.getEventStartTime().value ?: System.currentTimeMillis()
        event.endTime = viewModel.getEventEndTime().value ?: System.currentTimeMillis()
        viewModel.setWayaEvent(event)

        return true
    }
}