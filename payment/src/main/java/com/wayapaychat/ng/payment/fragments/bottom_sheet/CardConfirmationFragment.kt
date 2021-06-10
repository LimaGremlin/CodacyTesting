package com.wayapaychat.ng.payment.fragments.bottom_sheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentCardConfirmationBinding
import com.wayapaychat.ng.payment.dialogs.NewWalletButtonSheetDialog
import com.wayapaychat.ng.payment.managecard.ManageCardViewModel
import com.wayapaychat.ng.payment.managewallet.ManageWalletViewModel


class CardConfirmationFragment : Fragment(), NewWalletButtonSheetDialog.OnClickListener {

    private lateinit var binding: FragmentCardConfirmationBinding
    private lateinit var viewModel: ManageCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_card_confirmation, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageCardViewModel::class.java)

        requestPin()

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun requestPin() {
        val newFragment = NewWalletButtonSheetDialog(this, getString(R.string.top_up_via_card))
        newFragment.show(requireActivity().supportFragmentManager, "QuantityFragment")
    }

    override fun newWalletClicked(pin: String, action: String) {
        findNavController().navigate(R.id.action_cardConfirmationFragment_to_topupSuccessFragment)
//        ("call final endpoint to top up via card here")
//        val user = viewModel.getUserData().value ?: UserDataLocalModel()
//        viewModel.verifyUserPinAsync(pin, user.token, getString(R.string.delete_bank_account))
    }


}