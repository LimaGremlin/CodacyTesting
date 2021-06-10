package com.wayapaychat.ng.payment.managecard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.ng.payment.ManageCardActivity
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentAddCardSuccessBinding
import com.wayapaychat.ng.payment.databinding.FragmentCreateWalletSuccessBinding
import com.wayapaychat.ng.payment.managewallet.ManageWalletViewModel


class AddCardSuccessFragment : Fragment() {

    private lateinit var binding: FragmentAddCardSuccessBinding
    private lateinit var viewModel: ManageCardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_card_success, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageCardViewModel::class.java)

        binding.returnButton.setOnClickListener {

            val intent = Intent(requireActivity(), ManageCardActivity::class.java)
            startActivity(intent)
        }

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }


}