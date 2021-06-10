package com.wayapaychat.ng.payment.linkbvn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentBVNVerificationBinding


/**
 * A simple [Fragment] subclass.
 * Use the [BVNVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BVNVerificationFragment : Fragment() {

    private lateinit var binding: FragmentBVNVerificationBinding
    private lateinit var viewModel: LinkBVNViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_b_v_n_verification, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LinkBVNViewModel::class.java)

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_BVNVerificationFragment_to_BVNLinkedSuccessFragment)
        }


        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root

    }


}