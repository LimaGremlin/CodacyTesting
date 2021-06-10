package com.wayapay.ng.landingpage.donation

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapay.ng.landingpage.NewDonationActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentCreateDonationBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapaychat.remote.servicesutils.WayaDonation
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 * Use the [CreateDonationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateDonationFragment : Fragment() {

    private lateinit var binding: FragmentCreateDonationBinding
    private lateinit var viewModel: DonationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_donation, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DonationViewModel::class.java)
        viewModel.setPublishButtonText(getString(R.string.next))

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewDonationActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            if(check())
                findNavController().navigate(R.id.action_createDonationFragment_to_createDonationContinueFragment)
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun check():Boolean{

        val donation = viewModel.getDonation().value ?: WayaDonation()

        if(TextUtils.isEmpty(binding.title.text.toString())){
            binding.title.error = ""
            binding.title.requestFocus()
            return false
        }

        if(TextUtils.isEmpty(binding.organizersName.text.toString())){
            binding.organizersName.error = ""
            binding.organizersName.requestFocus()
            return false
        }

        if(TextUtils.isEmpty(binding.descriptionEditText.text.toString())){
            binding.descriptionEditText.error = ""
            binding.descriptionEditText.requestFocus()
            return false
        }

        donation.title = binding.title.text.toString()
        donation.organizerName = binding.organizersName.text.toString()
        donation.description = binding.descriptionEditText.text.toString()
        viewModel.setDonation(donation)

        return true
    }
}