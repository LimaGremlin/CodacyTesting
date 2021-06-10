package com.wayapaychat.ng.payment.fragments.bottom_sheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentTopupViaCardBinding
import com.wayapaychat.ng.payment.managecard.ManageCardViewModel

class TopupViaCardFragment : Fragment() {

   private lateinit var binding: FragmentTopupViaCardBinding
    private lateinit var viewModel: ManageCardViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_topup_via_card, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageCardViewModel::class.java)

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel

        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_topupViaCardFragment_to_cardConfirmationFragment)

        }

        return binding.root
    }

}