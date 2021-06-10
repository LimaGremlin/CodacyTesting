package com.wayapaychat.ng.payment.fragments.managecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.afterTextChanged
import com.wayapaychat.core.utils.getMonthDigits
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.BR
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentAddCardBinding
import com.wayapaychat.ng.payment.utils.getMonthUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCardFragment : BaseFragment<FragmentAddCardBinding, PaymentActivityViewModel>() {

    private lateinit var binding: FragmentAddCardBinding
    private val paymentViewModel: PaymentActivityViewModel by activityViewModels()
    private lateinit var items: List<String>
    private lateinit var phoneNumberDialog: AlertDialog

    override fun getViewModel(): PaymentActivityViewModel = paymentViewModel

    override fun getLayoutId(): Int = R.layout.fragment_add_card

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: FragmentAddCardBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paymentViewModel.cardAdded.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                navigate(WayaNavigationCommand.To(AddCardFragmentDirections.actionAddCardFragmentToCardVerifiedFragment("Card Verified")))
            }
        })
        paymentViewModel.navigateToOTPScreen.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                navigate(WayaNavigationCommand.To(AddCardFragmentDirections.actionAddCardFragmentToVerifyBvnFragment()))
            }
        })
        paymentViewModel.submitPhone.observe(viewLifecycleOwner, {
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
                    phoneNumberDialog.dismiss()
                    navigate(WayaNavigationCommand.To(AddCardFragmentDirections.actionAddCardFragmentToVerifyBvnFragment()))
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        items = getMonthUtils(requireContext())
        ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            items
        ).also {
            // Specify the layout to use when the list of choices appears
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.expiryMonth.adapter = it
        }

        setOnClickListeners()
        setTextWatchers()
        setObservers()
    }

    private fun setObservers() {
        with(paymentViewModel) {
            toastMessage.observe(viewLifecycleOwner, EventObserver {
                showSnackBar(binding.root, it, isWarning = true)
            })
            showConfirmationDialog.observe(viewLifecycleOwner, EventObserver {
                if (it) {
                    buildNoticeAlertDialog()
                }
            })
            addBankCard.observe(viewLifecycleOwner, EventObserver {
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
                    }
                }
            })
            showPhoneDialog.observe(viewLifecycleOwner, EventObserver {
                showUserPhoneDialog()
            })
        }
    }

    private fun showUserPhoneDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        val customAlertDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.enter_phone_dialog, null, false)
        val phoneNumber = customAlertDialogView.findViewById<TextInputEditText>(R.id.phone_number)
        customAlertDialogView.findViewById<MaterialButton>(R.id.submitBtn)
            .setOnClickListener {
                val enteredPhoneNumber = phoneNumber.text.toString()
                if (enteredPhoneNumber.length != 11) {
                    showSnackBar(binding.root, "Invalid phone number entered", true)
                } else {
                    paymentViewModel.verifyAssociatedPhoneNumber(enteredPhoneNumber)
                }
            }

        // Building the Alert dialog using materialAlertDialogBuilder instance
        phoneNumberDialog =
            materialAlertDialogBuilder.setView(customAlertDialogView)
                .show()
    }

    private fun setTextWatchers() {
        with(binding) {
            cardName.afterTextChanged {
                val cardName = it
                displayCardName(cardName)
            }
            cardNumber.afterTextChanged {
                val cardNumber = it
                displayCardNumber(cardNumber)
            }
            expiryYear.afterTextChanged {
                val expiryYear = it
                displayExpiryYear(expiryYear)
            }
        }
    }

    private fun displayExpiryYear(expiryYear: String) {
        if (expiryYear.length == 4) {
            val existingExpiryYear = binding.typedExpiryDate.text.toString()
            binding.typedExpiryDate.text =
                getString(R.string.expiry_date_concat, existingExpiryYear, expiryYear.substring(2))
        }
    }

    private fun displayCardNumber(cardNumber: String) {
        binding.typedCardNumber.text = cardNumber
    }

    private fun displayCardName(cardName: String) {
        binding.typedCardName.text = cardName
    }

    private fun setOnClickListeners() {
        with(binding) {
            addCardButton.setOnClickListener {
                with(binding) {
                    paymentViewModel.validateCardDetails(
                        cardName.text.toString(),
                        cardNumber.text.toString(),
                        cvvNumber.text.toString(),
                        expiryMonth.selectedItemPosition,
                        expiryYear.text.toString()
                    )
                }
            }
            toolbar.leftImage.setOnClickListener {
                navigate(WayaNavigationCommand.Back)
            }

            expiryMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        showMonthSelected(items[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun showMonthSelected(monthSelected: String) {
        if (binding.typedExpiryDate.text.isNotEmpty())
            binding.typedExpiryDate.text =
                binding.typedExpiryDate.text.replaceRange(0, 2, monthSelected.getMonthDigits())
        else
            binding.typedExpiryDate.text = monthSelected.getMonthDigits()
    }

    private fun buildNoticeAlertDialog() {
        //Build alert dialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.notice))
            .setMessage(
                "Dear customer, \n" +
                    "a charge of N10 will be deducted from your card to validate it’s " +
                    "authenticity. The money deducted will be added back to your wallet "
            )
            .setPositiveButton(getString(R.string.go_ahead)) { _, _ ->
                addCard()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }

    private fun addCard() {
        with(binding) {
            paymentViewModel.addCard(cardName.text.toString(),
                cardNumber.text.toString(),
                items[expiryMonth.selectedItemPosition],
                expiryYear.text.toString(),
                cardPin.text.toString(),
                cvvNumber.text.toString()
            )
        }
    }
}
