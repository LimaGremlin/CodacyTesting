package com.wayapay.ng.landingpage.donation

import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapay.ng.landingpage.NewDonationActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentCreateDonationContinueBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapaychat.remote.servicesutils.WayaDonation

/**
 * A simple [Fragment] subclass.
 * Use the [CreateDonationContinueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateDonationContinueFragment : Fragment() {

    private lateinit var binding: FragmentCreateDonationContinueBinding
    private lateinit var viewModel: DonationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_donation_continue, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DonationViewModel::class.java)
        viewModel.setPublishButtonText(getString(R.string.next))

        binding.termsAndCondition.movementMethod = LinkMovementMethod.getInstance()

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewDonationActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            if(check())
                findNavController().navigate(R.id.action_createDonationContinueFragment_to_createDonationOverviewFragment)
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    fun check():Boolean{

        val donation = viewModel.getDonation().value ?: WayaDonation()

        if(TextUtils.isEmpty(binding.donationTarget.text.toString())){
            binding.donationTarget.error = ""
            binding.donationTarget.requestFocus()
            return false
        }

        if((!binding.displayContribution.isChecked) && (!binding.hideContribution.isChecked)){
            binding.privateMessage.error = ""
            binding.checkGroup.requestFocus()
            return false
        }

        if(!binding.termsAndConditionCheck.isChecked){
            binding.termsAndCondition.error = ""
            binding.termsAndCondition.requestFocus()
            return false
        }

        donation.target = binding.donationTarget.text.toString().toDoubleOrNull() ?: 0.0
        donation.displayTotalDonation = binding.displayContribution.isChecked

        viewModel.setDonation(donation)

        return true
    }
}