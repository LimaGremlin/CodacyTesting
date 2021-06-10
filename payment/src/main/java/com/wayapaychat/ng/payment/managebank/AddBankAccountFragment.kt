package com.wayapaychat.ng.payment.managebank

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentAddBankAccountBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddBankAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddBankAccountFragment : Fragment() {

    private lateinit var binding: FragmentAddBankAccountBinding
    private lateinit var viewModel: ManageBankViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_bank_account, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageBankViewModel::class.java)

        viewModel.getListAllBanks().observe(requireActivity(), Observer { list ->
            // Create an ArrayAdapter using the string array and a default spinner layout
            val items = list.map { it.name }
            val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
            adapter.also {
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.addBankSpinner.adapter = adapter
            }
        })

        binding.submitButton.setOnClickListener {
            check()
        }

        viewModel.setTextHeader(getString(R.string.add_bank_account))
        viewModel.setAddOptionVisibility(false)

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun check():Boolean{

        if(binding.addBankSpinner.selectedItemPosition == 0){
            Toast.makeText(requireContext().applicationContext, getString(R.string.select_a_bank), Toast.LENGTH_SHORT).show()
            return false
        }

        if(TextUtils.isEmpty(binding.accountNumber.text.toString())){
            binding.accountNumber.error = ""
            binding.accountNumber.requestFocus()
            return false
        }

        if(TextUtils.isEmpty(binding.accountName.text.toString())){
            binding.accountName.error = ""
            binding.accountName.requestFocus()
            return false
        }

        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)

        val user = viewModel.getUserData().value ?: UserDataLocalModel()
        val allBanks = viewModel.getAllBanks(binding.addBankSpinner.selectedItemPosition)

        val map = hashMapOf(
            "accountName" to binding.accountName.text.toString(),
            "accountNumber" to binding.accountNumber.text.toString(),
            "bankCode" to allBanks.code,
            "bankName" to allBanks.name,
            //125 is Rubies bank code gotten from list of all banks
            "rubiesBankCode" to "125",
            "userId" to id.toString()
        )

        //Add bank to server
        viewModel.addBankAccount(map, user.token, findNavController())
        return true
    }
}