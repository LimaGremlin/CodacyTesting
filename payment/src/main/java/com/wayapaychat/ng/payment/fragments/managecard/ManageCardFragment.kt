package com.wayapaychat.ng.payment.fragments.managecard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.adapters.SingleLayoutRecyclerAdapter
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.listeners.BindableItemClickListener
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.ng.payment.BR
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.CardsRecItemBinding
import com.wayapaychat.ng.payment.databinding.ManageCardFragmentBinding
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ManageCardFragment : BaseFragment<ManageCardFragmentBinding, PaymentActivityViewModel>(),
    BindableItemClickListener<UserBankCard> {

    private val cardsAdapter by lazy {
        object : SingleLayoutRecyclerAdapter<CardsRecItemBinding, UserBankCard>(
            context = context!!,
            itemClickListener = this@ManageCardFragment,
            layoutId = R.layout.cards_rec_item
        ) {
            override fun getItemBindingVariable(): Int = BR.bankCard

            override fun getItemClickListenerBindingVariable(): Int = BR.Listener
        }
    }

     val paymentViewModel: PaymentActivityViewModel by activityViewModels()
    lateinit var binding: ManageCardFragmentBinding

    override fun getViewModel(): PaymentActivityViewModel = paymentViewModel

    override fun getLayoutId(): Int = R.layout.manage_card_fragment

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: ManageCardFragmentBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paymentViewModel.moveToClickedCard.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                //navigate(WayaNavigationCommand.To(ManageCardFragmentDirections.actionManageCardFragmentToCardDetailsFragment()))
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        paymentViewModel.getUserBankCards()
        setOnClickListener()
        setUpAdapter()
      //  setObservers()
    }

    private fun setObservers() {
        with(paymentViewModel) {
            userBankCards.observe(viewLifecycleOwner, {
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
                        showUserCards(it.data)
                    }
                }
            })
        }
    }

    private fun showUserCards(data: List<UserBankCard>?) {
        data?.let {
            cardsAdapter.items = it
        }
    }

    private fun setUpAdapter() {
        binding.cardRecView.apply {
            adapter = cardsAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 500
                removeDuration = 500
                changeDuration = 500
                moveDuration = 500
            }
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            emptyStateCards.emptyBtn.setOnClickListener {
                //navigate(WayaNavigationCommand.To(ManageCardFragmentDirections.actionManageCardFragmentToAddCardFragment()))
            }
            with(toolbar) {
                leftImage.setOnClickListener {
                    navigate(WayaNavigationCommand.Back)
                }
                rightImage.setOnClickListener {
                    //navigate(WayaNavigationCommand.To(ManageCardFragmentDirections.actionManageCardFragmentToAddCardFragment()))
                }
            }
        }
    }

    override fun onItemClicked(item: UserBankCard) {
        paymentViewModel.setItemAndNavigate(item)
    }
}
