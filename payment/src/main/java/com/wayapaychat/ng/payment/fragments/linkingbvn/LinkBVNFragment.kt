package com.wayapaychat.ng.payment.fragments.linkingbvn

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.Helpers.reveal
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.LinkBvnFragmentBinding
import com.wayapaychat.ng.payment.databinding.WhyBvnDialogBinding
import kotlinx.android.synthetic.main.link_bvn_fragment.*

class LinkBVN : BaseFragment<LinkBvnFragmentBinding, PaymentActivityViewModel>() {

    private val paymentActivityViewModel: PaymentActivityViewModel by activityViewModels()
    private lateinit var binding: LinkBvnFragmentBinding
    lateinit var whyBVNDialog: AlertDialog

    override fun getViewModel(): PaymentActivityViewModel = paymentActivityViewModel

    override fun getLayoutId(): Int = R.layout.link_bvn_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: LinkBvnFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        setOnClickListeners()
//        setObservers()
//    }

//    private fun setObservers() {
//        with(paymentActivityViewModel) {
//            toastMessage.observe(viewLifecycleOwner, EventObserver {
//                showSnackBar(binding.root, it, isWarning = true)
//            })
//            showNoMatchBvn.observe(viewLifecycleOwner, EventObserver {
//                if (it) {
//                    noMatchBvn.reveal()
//                }
//            })
//        }
//    }
//
//    private fun setOnClickListeners() {
//        with(binding) {
//            whyBvn.setOnClickListener {
//                showWhyBVNDialog()
//            }
//        }
//    }
//
//    private fun showWhyBVNDialog() {
//        whyBVNDialog = createDoubleButtonDialog<WhyBvnDialogBinding>(
//            R.layout.why_bvn_dialog,
//            binding.root as ViewGroup,
//            positiveButtonClickListener = {},
//            negativeButtonClickListener = {
//                whyBVNDialog.dismiss()
//            }
//        )
//        whyBVNDialog.show()
    }
}
