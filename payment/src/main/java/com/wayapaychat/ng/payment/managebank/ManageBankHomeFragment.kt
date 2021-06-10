package com.wayapaychat.ng.payment.managebank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentManageBankHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ManageBankHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageBankHomeFragment : Fragment(), BankAccountAdapter.OnClickListener {

    private lateinit var binding: FragmentManageBankHomeBinding
    private lateinit var viewModel: ManageBankViewModel
    private lateinit var accAdapter: BankAccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_manage_bank_home, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageBankViewModel::class.java)

        viewModel.setTextHeader(getString(R.string.manage_banks))
        viewModel.setAddOptionVisibility(true)

        accAdapter = BankAccountAdapter(this)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = accAdapter
        }

        viewModel.getListWayaAccounts().observe(requireActivity(), Observer {
            if(it.isNullOrEmpty()){
                binding.recyclerView.visibility = View.GONE
                binding.emptyLayout.visibility = View.VISIBLE
            }
            else {
                binding.emptyLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
            accAdapter.swapList(it)
        })

        binding.addBankButton.setOnClickListener {
            findNavController().navigate(R.id.action_manageBankHomeFragment_to_addBankAccountFragment)
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }


    override fun onAccountClicked(position: Int, tag: String) {
        //TODO("Not yet implemented")
        val acc = accAdapter.getAccount(position)
        viewModel.setWayaAccounts(acc)
        findNavController().navigate(R.id.action_manageBankHomeFragment_to_bankDetailsFragment)
    }
}