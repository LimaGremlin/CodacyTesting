package com.wayapaychat.ng.payment.managecard

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.models.TempWallet
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentAddCardBinding
import com.wayapaychat.ng.payment.databinding.FragmentNewCardBinding
import com.wayapaychat.ng.payment.utils.getMonthUtils


class NewCardFragment : Fragment() {

    private lateinit var binding: FragmentNewCardBinding
    private lateinit var viewModel: ManageCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_new_card, container, false)

        val viewModelFactory =
            PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(ManageCardViewModel::class.java)

        val items = getMonthUtils(requireContext())
        val adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
        adapter.also {
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.expiryMonth.adapter = adapter
        }

        binding.submit.setOnClickListener {

            check()

        }



        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun check(): Boolean {

        if (binding.expiryMonth.selectedItemPosition == 0) {
            Toast.makeText(
                requireContext().applicationContext,
                getString(R.string.select_month),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (TextUtils.isEmpty(binding.cardName.text.toString())) {
            binding.cardName.error = ""
            binding.cardName.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(binding.cardNumber.text.toString())) {
            binding.cardNumber.error = ""
            binding.cardNumber.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(binding.expiryYear.text.toString())) {
            binding.expiryYear.error = ""
            binding.expiryYear.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(binding.cvvNumber.text.toString())) {
            binding.cvvNumber.error = ""
            binding.cvvNumber.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(binding.cardPin.text.toString())) {
            binding.cardPin.error = ""
            binding.cardPin.requestFocus()
            return false
        }

        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)

        val user = viewModel.getUserData().value ?: UserDataLocalModel()
        val wallet = viewModel.getWallet().value ?: TempWallet()
        val email = user.email
        val cardNo = binding.cardNumber.text.toString()
        val map = hashMapOf(
            "cardNumber" to cardNo,
            "cvv" to binding.cvvNumber.text.toString(),
            "email" to email,
            "expiryMonth" to binding.expiryMonth.selectedItemPosition.toString(),
            "expiryYear" to binding.expiryYear.text.toString(),
            "name" to binding.cardName.text.toString(),
            "last4digit" to cardNo.substring(0, cardNo.length - 3),
            "pin" to binding.cardPin.text.toString(),
            "userId" to id.toString(),
            "walletAccountNumber" to wallet.accountNo,

        )

        //Add bank to server
      createDialog(map, user.token, this.findNavController())
        return true
    }
    
    private fun createDialog(map: HashMap<String, String>, userToken: String, navController: NavController){
        //Build alert dialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.notice))
            .setMessage(
                "Dear customer, \n" +
                        "a charge of N10 will be deducted from your card to validate it’s " +
                        "authenticity. The money deducted will be added back to your wallet "
            )
            .setPositiveButton(getString(R.string.go_ahead)) { _, _ ->
                  viewModel.addCard(map, userToken, this.findNavController())
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }
}
