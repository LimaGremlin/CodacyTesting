package com.wayapaychat.ng.payment.linkbvn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentBVNLinkedSuccessBinding


/**
 * A simple [Fragment] subclass.
 * Use the [BVNLinkedSuccessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BVNLinkedSuccessFragment : Fragment() {

    private lateinit var binding: FragmentBVNLinkedSuccessBinding
    private lateinit var viewModel: LinkBVNViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_b_v_n_linked_success, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LinkBVNViewModel::class.java)

        binding.returnButton.setOnClickListener {
            requireActivity().finish()
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }



}