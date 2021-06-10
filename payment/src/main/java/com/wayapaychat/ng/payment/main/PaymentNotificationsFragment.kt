package com.wayapaychat.ng.payment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentPaymentNotificationsBinding
import com.wayapaychat.ng.payment.model.PaymentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentNotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentNotificationsFragment : Fragment() {

    private lateinit var binding: FragmentPaymentNotificationsBinding
    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_payment_notifications, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PaymentViewModel::class.java)

        viewModel.setTextHeader(getString(R.string.notifications))

        binding.navButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }
}