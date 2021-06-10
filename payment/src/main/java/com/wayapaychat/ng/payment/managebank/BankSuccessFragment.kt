package com.wayapaychat.ng.payment.managebank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentBankSuccessBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BankSuccessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BankSuccessFragment : Fragment() {

    private lateinit var binding: FragmentBankSuccessBinding
    private lateinit var viewModel: ManageBankViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bank_success, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageBankViewModel::class.java)

        viewModel.setTextHeader(getString(R.string.add_bank_account))
        viewModel.setAddOptionVisibility(false)

        binding.returnButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }
}