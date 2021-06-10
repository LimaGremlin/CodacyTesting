package com.wayapaychat.ng.payment.managecard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentChooseWalletBinding
import com.wayapaychat.ng.payment.databinding.FragmentMangeWalletHomeBinding
import com.wayapaychat.ng.payment.managewallet.ManageWalletViewModel
import com.wayapaychat.ng.payment.managewallet.WalletAdapter
import kotlinx.android.synthetic.main.payment_landing_nav_header.*

class ChooseWalletFragment : Fragment(),  ChooseWalletAdapter.OnWalletClickListener {

    private lateinit var binding: FragmentChooseWalletBinding
    private lateinit var viewModel: ManageCardViewModel
    private lateinit var walletAdapter: ChooseWalletAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_choose_wallet, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageCardViewModel::class.java)
        viewModel.setAddWalletVisibility(false)


        walletAdapter = ChooseWalletAdapter(this)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = walletAdapter
        }

        viewModel.setTextHeader(getString(R.string.choose_wallet))

        viewModel.getListWallets().observe(requireActivity(), Observer {
            walletAdapter.swapList(it)
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root

    }

    override fun onWalletClicked(position: Int, tag: String) {
        val wallet = walletAdapter.getWallet(position)
        viewModel.setWallet(wallet)


        //navigate to add card with bundle
        //ensure user selects card to fund
        findNavController().navigate(R.id.action_chooseWalletFragment2_to_newCardFragment)

    }


}