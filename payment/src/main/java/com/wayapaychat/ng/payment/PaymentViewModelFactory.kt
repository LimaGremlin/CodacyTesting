package com.wayapaychat.ng.payment

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.ng.payment.linkbvn.LinkBVNViewModel
import com.wayapaychat.ng.payment.managebank.ManageBankViewModel
import com.wayapaychat.ng.payment.managecard.CardsViewModel
import com.wayapaychat.ng.payment.managecard.ManageCardViewModel
import com.wayapaychat.ng.payment.managewallet.ManageWalletViewModel
import com.wayapaychat.ng.payment.model.PaymentViewModel

@Suppress("UNCHECKED_CAST")
class PaymentViewModelFactory(private val activity: Activity, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LinkBVNViewModel::class.java) ->
                LinkBVNViewModel(activity, application) as T
            modelClass.isAssignableFrom(ManageBankViewModel::class.java) ->
                ManageBankViewModel(activity, application) as T
            modelClass.isAssignableFrom(ManageCardViewModel::class.java) ->
                ManageCardViewModel(activity, application) as T
            modelClass.isAssignableFrom(ManageWalletViewModel::class.java) ->
                ManageWalletViewModel(activity, application) as T
            modelClass.isAssignableFrom(PaymentViewModel::class.java) ->
                PaymentViewModel(activity, application) as T
            modelClass.isAssignableFrom(CardsViewModel::class.java) ->
                CardsViewModel(activity, application) as T
            else -> throw IllegalArgumentException("Unknown class")
        }
    }
}