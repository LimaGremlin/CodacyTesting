package com.wayapay.ng.landingpage.donation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapay.ng.landingpage.NewDonationActivity
import com.wayapay.ng.landingpage.NewPostActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentCreateDonationOverviewBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.WayaEvent
import com.wayapaychat.remote.servicesutils.WayaDonation

/**
 * A simple [Fragment] subclass.
 * Use the [CreateDonationOverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateDonationOverviewFragment : Fragment() {

    private lateinit var binding: FragmentCreateDonationOverviewBinding
    private lateinit var viewModel: DonationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_donation_overview, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DonationViewModel::class.java)
        viewModel.setPublishButtonText(getString(R.string.publish))

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewDonationActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val donation = viewModel.getDonation().value ?: WayaDonation()

            val returnIntent = Intent()
            returnIntent.putExtra(IntentBundles.DONATION_KEY, donation)
            requireActivity().setResult(Activity.RESULT_OK, returnIntent)
            requireActivity().finish()
        }

        binding.imageButton.setOnClickListener {
            (requireActivity() as NewDonationActivity).buildImagePicker()
        }

        binding.imageButton2.setOnClickListener {
            (requireActivity() as NewDonationActivity).buildImagePicker()
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }
}