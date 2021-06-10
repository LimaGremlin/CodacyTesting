package com.wayapaychat.ng.payment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentAddBankBinding
import com.wayapaychat.ng.payment.model.PaymentViewModel

class AddBankFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentAddBankBinding
    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_bank, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PaymentViewModel::class.java)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val items = listOf("Select Bank", "Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
        adapter.also {
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.bankSpinner.adapter = adapter
        }

        binding.bankSpinner.onItemSelectedListener = this

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //TODO("Not yet implemented")
    }
}
