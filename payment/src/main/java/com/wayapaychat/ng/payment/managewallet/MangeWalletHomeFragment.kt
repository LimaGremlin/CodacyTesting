package com.wayapaychat.ng.payment.managewallet

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
import com.wayapaychat.ng.payment.databinding.FragmentMangeWalletHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MangeWalletHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MangeWalletHomeFragment : Fragment(), WalletAdapter.OnWalletClickListener {

    private lateinit var binding: FragmentMangeWalletHomeBinding
    private lateinit var viewModel: ManageWalletViewModel
    private lateinit var walletAdapter: WalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_mange_wallet_home, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageWalletViewModel::class.java)

        viewModel.setTextHeader(getString(R.string.my_wallets))
        viewModel.setAddWalletVisibility(true)
        viewModel.setOptionsVisibility(false)

        walletAdapter = WalletAdapter(this)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = walletAdapter
        }

        viewModel.setTextHeader(getString(R.string.my_wallets))

        viewModel.getListTempWallets().observe(requireActivity(), Observer {
            walletAdapter.swapList(it)
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onWalletClicked(position: Int, tag: String) {
        val wallet = walletAdapter.getWallet(position)
        viewModel.setTempWallet(wallet)
        findNavController().navigate(R.id.action_mangeWalletFragment_to_walletDetailsFragment)
    }
}