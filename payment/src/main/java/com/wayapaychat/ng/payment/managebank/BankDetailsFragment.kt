package com.wayapaychat.ng.payment.managebank

import android.app.AlertDialog
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
import com.wayapaychat.ng.payment.databinding.FragmentBankDetailsBinding
import com.wayapaychat.ng.payment.dialogs.NewWalletButtonSheetDialog

/**
 * A simple [Fragment] subclass.
 * Use the [BankDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BankDetailsFragment : Fragment(), NewWalletButtonSheetDialog.OnClickListener {

    private lateinit var binding: FragmentBankDetailsBinding
    private lateinit var viewModel: ManageBankViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bank_details, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageBankViewModel::class.java)

        viewModel.setTextHeader(getString(R.string.bank_details))
        viewModel.setAddOptionVisibility(false)

        binding.button3.setOnClickListener {
            showDialog()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    private fun showDialog(){
        //Build alert dialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.delete_account))
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_account))
        builder.setPositiveButton(getString(R.string.continue_)){ _, _ ->
            val newFragment = NewWalletButtonSheetDialog(this, getString(R.string.delete_bank_account))
            newFragment.show(requireActivity().supportFragmentManager, "QuantityFragment")
        }
        builder.setNegativeButton(getString(R.string.close)) { dialog, _ ->
            //Dismiss dialog on cancel
            dialog.dismiss()
        }.show()
    }

    override fun newWalletClicked(pin: String, action: String) {
        val user = viewModel.getUserData().value ?: UserDataLocalModel()
        viewModel.verifyUserPinAsync(pin, user.token, getString(R.string.delete_bank_account))
    }
}