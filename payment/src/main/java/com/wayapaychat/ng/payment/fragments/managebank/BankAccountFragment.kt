package com.wayapaychat.ng.payment.fragments.managebank

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.BR
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentBankAccountBinding
import com.wayapaychat.ng.payment.model.BankAccount
import com.wayapaychat.ng.payment.viewModels.BankAccountViewModel
import dagger.hilt.android.AndroidEntryPoint

const val ARG_BANK_ACCOUNT = "bank_account"

@AndroidEntryPoint
class BankAccountFragment : BaseFragment<FragmentBankAccountBinding, BankAccountViewModel>() {

    private var param1: BankAccount? = null
    private val bankAccountViewModel by viewModels<BankAccountViewModel>()
    private lateinit var binding: FragmentBankAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_BANK_ACCOUNT)
        }
    }

    override fun getViewModel(): BankAccountViewModel =
        bankAccountViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_bank_account

    override fun getBindingVariable(): Int =
        BR.viewModel

    override fun getLayoutBinding(binding: FragmentBankAccountBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        param1?.let {
            bankAccountViewModel.setBankAccount(it)
        }
        setupObservers()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            deleteBankAccountBtn.setOnClickListener {
                showDeleteDialog()
            }
        }
    }

    private fun showDeleteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.notice))
            .setMessage(
                "Dear customer, are you sure you want to permanently delete this bank account?"
            )
            .setPositiveButton(getString(R.string.go_ahead)) { _, _ ->
                bankAccountViewModel.deleteBankAccount(param1?.accountNumber!!)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }

    private fun setupObservers() {
        with(bankAccountViewModel) {
            deleteBankAccountLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message ->
                            showSnackBar(binding.root, message, true)
                        }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        showSnackBar(binding.root, getString(R.string.bank_account_deleted), false)
                        findNavController().popBackStack()
                    }
                }
            })
        }
    }
}
