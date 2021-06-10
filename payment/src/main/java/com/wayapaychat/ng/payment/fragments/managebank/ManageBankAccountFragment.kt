package com.wayapaychat.ng.payment.fragments.managebank

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.adapters.SingleLayoutRecyclerAdapter
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.listeners.BindableItemClickListener
import com.wayapaychat.core.utils.Helpers.hide
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.show
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.BR
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.BankAccountItemBinding
import com.wayapaychat.ng.payment.databinding.ManageBankAccountFragmentBinding
import com.wayapaychat.ng.payment.model.BankAccount
import com.wayapaychat.ng.payment.viewModels.ManageBankAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.manage_bank_account_fragment.*

@AndroidEntryPoint
class ManageBankAccountFragment :
    BaseFragment<ManageBankAccountFragmentBinding, ManageBankAccountViewModel>(), BindableItemClickListener<BankAccount> {

    private val manageBanksAccountViewModel: ManageBankAccountViewModel by activityViewModels()

    lateinit var binding: ManageBankAccountFragmentBinding

    override fun getViewModel(): ManageBankAccountViewModel = manageBanksAccountViewModel

    override fun getLayoutId(): Int = R.layout.manage_bank_account_fragment

    override fun getBindingVariable(): Int = 0

    private val bankAccountsAdapter by lazy {
        object : SingleLayoutRecyclerAdapter<BankAccountItemBinding, BankAccount>(
            context = requireContext(),
            itemClickListener = this@ManageBankAccountFragment,
            layoutId = R.layout.bank_account_item
        ) {
            override fun getItemBindingVariable(): Int = BR.bankAccount

            override fun getItemClickListenerBindingVariable(): Int = BR.Listener
        }
    }

    override fun getLayoutBinding(binding: ManageBankAccountFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
        setUpAdapter()
        setupObservers()
        manageBanksAccountViewModel.getBankAccounts()
    }

    private fun setOnClickListeners() {
        with(binding) {
            emptyStateCards.emptyBtn.setOnClickListener {
                navigate(WayaNavigationCommand.To(ManageBankAccountFragmentDirections.actionManageBankAccountFragmentToAddBankAccount()))
            }
            with(toolbar) {
                rightImage.setOnClickListener {
                    navigate(WayaNavigationCommand.To(ManageBankAccountFragmentDirections.actionManageBankAccountFragmentToAddBankAccount()))
                }
                leftImage.setOnClickListener {
                    navigate(WayaNavigationCommand.Back)
                }
            }
            retryBtn.setOnClickListener {
                retryBtn.hide()
                manageBanksAccountViewModel.getBankAccounts()
            }
        }
    }

    private fun setupObservers(){
        with(manageBanksAccountViewModel){
            getBankAccountsLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message ->
                            showSnackBar(binding.root, message, true)
                        }
                        retryBtn.show()
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        it.data?.let {
                            if (it.isEmpty()){
                                binding.emptyStateCards.root.show()
                                binding.bankAccountsRV.hide()
                            }else{
                                binding.emptyStateCards.root.hide()
                                binding.bankAccountsRV.show()
                                bankAccountsAdapter.items = it
                            }
                        }
                    }
                }
            })
        }
    }

    private fun setUpAdapter() {
        binding.bankAccountsRV.apply {
            adapter = bankAccountsAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 500
                removeDuration = 500
                changeDuration = 500
                moveDuration = 500
            }
        }
    }

    override fun onItemClicked(item: BankAccount) {
        findNavController().navigate(
            R.id.action_manageBankAccountFragment_to_bankAccountFragment,
            Bundle().apply {
                putParcelable(ARG_BANK_ACCOUNT, item)
            }
        )
    }

}
