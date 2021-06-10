package com.wayapaychat.ng.payment.managewallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentCreateWalletSuccessBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CreateWalletSuccessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateWalletSuccessFragment : Fragment() {

    private lateinit var binding: FragmentCreateWalletSuccessBinding
    private lateinit var viewModel: ManageWalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_wallet_success, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageWalletViewModel::class.java)

        viewModel.setTextHeader(getString(R.string.my_wallets))
        viewModel.setAddWalletVisibility(false)
        viewModel.setOptionsVisibility(false)

        binding.returnButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }
}