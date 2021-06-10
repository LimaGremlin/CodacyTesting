package com.wayapaychat.ng.payment.managewallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.models.TempWallet
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentWalletDetailsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [WalletDetailsFragment.newInstance] factory method toB
 * create an instance of this fragment.
 */
class WalletDetailsFragment : Fragment() {

    private lateinit var binding: FragmentWalletDetailsBinding
    private lateinit var viewModel: ManageWalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_wallet_details,
            container,
            false
        )

        val viewModelFactory =
            PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(ManageWalletViewModel::class.java)

        viewModel.setOptionsVisibility(true)
        viewModel.setAddWalletVisibility(false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        binding.scanQr.setOnClickListener{
            findNavController().navigate(R.id.action_walletDetailsFragment_to_generateQrFragment)
        }
        return binding.root
    }
}