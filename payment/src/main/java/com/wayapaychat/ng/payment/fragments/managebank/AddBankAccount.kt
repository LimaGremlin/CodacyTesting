package com.wayapaychat.ng.payment.fragments.managebank

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.showShortToast
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.AddBankAccountFragmentBinding
import com.wayapaychat.ng.payment.model.Bank
import com.wayapaychat.ng.payment.model.ResolveAccountNumberResponse
import com.wayapaychat.ng.payment.viewModels.AddBankAccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBankAccount : BaseFragment<AddBankAccountFragmentBinding, AddBankAccountViewModel>() {

    private val addBankAccountViewModel: AddBankAccountViewModel by viewModels()
    private lateinit var binding: AddBankAccountFragmentBinding
    private lateinit var banks: List<Bank>
    private var selectedBank: Bank? = null
    private var resolveAccountNumberResponse: ResolveAccountNumberResponse? = null
    private var accountNumberResolved = false

    override fun getViewModel(): AddBankAccountViewModel = addBankAccountViewModel

    override fun getLayoutId(): Int = R.layout.add_bank_account_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: AddBankAccountFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
        setupObservers()
        addBankAccountViewModel.getBanks()
    }

    private fun setOnClickListeners() {
        with(binding) {
            with(toolbar) {
                leftImage.setOnClickListener {
                    navigate(WayaNavigationCommand.Back)
                }
            }

            actionBtn.setOnClickListener {
                val accountNumberText = accountNumber.text.toString()
                when {

                    accountNumberText.isEmpty() -> {
                        requireContext().showShortToast(R.string.enter_account_number)
                    }

                    selectedBank == null -> {
                        requireContext().showShortToast(R.string.error_select_bank)
                    }

                    !accountNumberResolved -> {
                        addBankAccountViewModel.resolveAccountNumber(accountNumberText, selectedBank!!.code)
                    }

                    accountNumberResolved -> {
                        addBankAccountViewModel.addBankAccount(
                            resolveAccountNumberResponse!!.accountName,
                            accountNumberText,
                            selectedBank!!.code,
                            selectedBank!!.name
                        )
                    }

                }
            }
        }
    }

    private fun setupObservers() {
        with(addBankAccountViewModel) {
            getBanksLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle(R.string.error_title)
                            setMessage(it.message)
                            setPositiveButton(R.string.try_again) { _, _ ->
                                getBanks()
                            }
                            setNegativeButton(R.string.cancel) { _, _ ->
                                findNavController().popBackStack()
                            }
                            show()
                        }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()

                        it.data?.let {
                            banks = it
                            val bankNames = mutableListOf<String>()

                            for (bank in it) {
                                bankNames.add(bank.name)
                            }

                            val bankAdapter = ArrayAdapter(
                                requireActivity(), R.layout.spinner_adapter_item, bankNames
                            )
                            binding.selectBankACTV.setAdapter(bankAdapter)

                            binding.selectBankACTV.setOnItemClickListener { _, _, position, _ ->
                                selectedBank = banks[position]
                            }
                        }
                    }
                }
            })

            resolveAccountNumberLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message ->
                            showSnackBar(binding.root, message, true)
                        }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()

                        it.data?.let {
                            resolveAccountNumberResponse = it
                            accountNumberResolved = true
                            with(binding) {
                                actionBtn.setText(R.string.add_bank_account)
                                accountName.setText(it.accountName)
                                accountNumber.addTextChangedListener {
                                    if (it.toString() != resolveAccountNumberResponse!!.accountNumber) {
                                        accountNumberResolved = false
                                        binding.actionBtn.setText(R.string.verify_account_number)
                                    } else {
                                        accountNumberResolved = true
                                        binding.actionBtn.setText(R.string.add_bank_account)
                                    }
                                }
                            }
                        }
                    }
                }
            })

            addBankAccountLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message ->
                            showSnackBar(binding.root, message, true)
                        }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        showSnackBar(binding.root, getString(R.string.bank_account_added), false)
                        findNavController().popBackStack()
                    }
                }
            })

        }
    }

}
