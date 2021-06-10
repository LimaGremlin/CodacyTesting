package com.wayapaychat.ng.payment.fragments.completeTransaction

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.showShortToast
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentConfirmTransactionBinding
import com.wayapaychat.ng.payment.model.Wallet
import com.wayapaychat.ng.payment.utils.TRANSACTION_ACCOUNT_NUMBER
import com.wayapaychat.ng.payment.utils.TRANSACTION_AMOUNT
import com.wayapaychat.ng.payment.utils.TRANSACTION_DESCRIPTION
import com.wayapaychat.ng.payment.utils.TRANSACTION_USER_NAME
import com.wayapaychat.ng.payment.viewModels.ConfirmTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject


const val ARG_TRANSACTION_DETAILS = "transaction_details"

@AndroidEntryPoint
class ConfirmTransactionFragment
    : BaseFragment<FragmentConfirmTransactionBinding, ConfirmTransactionViewModel>(), View.OnClickListener {

    private var amount = 0
    private var beneficiary = ""
    private var accountNumber = ""
    private var description = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString(ARG_TRANSACTION_DETAILS)?.let {
                val transactionDetails = JSONObject(it)
                try {
                    beneficiary = transactionDetails.getString(TRANSACTION_USER_NAME)
                    amount = transactionDetails.getInt(TRANSACTION_AMOUNT)
                    accountNumber = transactionDetails.getString(TRANSACTION_ACCOUNT_NUMBER)
                    description = transactionDetails.getString(TRANSACTION_DESCRIPTION)
                }catch (e: JSONException){

                }
            }
        }
    }

    private val confirmTransactionViewModel: ConfirmTransactionViewModel by viewModels()
    private val activityViewModel by activityViewModels<PaymentActivityViewModel>()
    private lateinit var binding: FragmentConfirmTransactionBinding
    private lateinit var wallets: List<Wallet>
    private lateinit var walletSelectionDialog: AlertDialog
    private lateinit var selectedWalletAccountNo: String

    override fun getViewModel(): ConfirmTransactionViewModel =
        confirmTransactionViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_confirm_transaction

    override fun getBindingVariable(): Int =
        0

    override fun getLayoutBinding(binding: FragmentConfirmTransactionBinding) {
       this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
        setupViews()
    }

    private fun setupViews(){
        with(binding){
            beneficiaryTV.text = beneficiary
            amountTV.text = "N$amount"
            accountNumberTV.text = accountNumber
            descriptionTV.text = description
        }
    }

    private fun setupClickListeners(){
        with(binding){
            backIV.setOnClickListener(this@ConfirmTransactionFragment)
            confirmMB.setOnClickListener(this@ConfirmTransactionFragment)
        }
    }

    override fun onClick(v: View) {
        when(v.id){

            R.id.back_IV -> findNavController().popBackStack()

            R.id.confirm_MB -> {
                val pin = binding.pinInput.text.toString()

                if (pin.isEmpty() || pin.length < 4){
                    requireContext().showShortToast(R.string.error_invalid_pin)
                }else{
                    walletSelectionDialog.show()
                }

            }

        }
    }

    private fun setupObservers(){
        with(activityViewModel){
            userWallets.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {

                        dismissLoadingDialog()

                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle(R.string.error_title)
                            setMessage(it.message)
                            setPositiveButton(R.string.try_again) { _, _ ->
                                getUserWallets()
                            }
                            setNegativeButton(R.string.cancel) { _, _ ->
                                findNavController().popBackStack()
                            }
                            show()
                        }

                    }

                    WayaAppState.SUCCESS -> {
                        it.data?.let { wallets ->
                            val walletNames = mutableListOf<String>()

                            this@ConfirmTransactionFragment.wallets = wallets
                            for (wallet in wallets) {
                                var text = wallet.accountName

                                if (wallet.default) {
                                    text += "(default)"
                                }

                                walletNames.add(text)
                            }
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle(R.string.select_wallet)
                            builder.setItems(
                                walletNames.toTypedArray()
                            ) { _, which ->
                                selectedWalletAccountNo = wallets[which].accountNo
                                confirmTransactionViewModel.verifyUserPin(binding.pinInput.text.toString())
                            }
                            walletSelectionDialog = builder.create()
                        }

                        dismissLoadingDialog()
                    }
                }
            })
        }

        with(confirmTransactionViewModel){
            verifyUserPinLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        performWalletToWalletTransaction(amount, selectedWalletAccountNo, accountNumber)
                    }
                }
            })

            performWalletToWalletTransactionLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        findNavController().navigate(
                            R.id.action_confirmTransactionFragment_to_transactionSuccessfulFragment,
                            Bundle().apply {
                                putInt(TRANSACTION_AMOUNT, amount)
                                putString(TRANSACTION_USER_NAME, beneficiary)
                            }
                        )
                    }
                }
            })
        }
    }

}
