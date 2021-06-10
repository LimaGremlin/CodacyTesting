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
import com.wayapaychat.ng.payment.databinding.FragmentLinkBVNHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LinkBVNHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LinkBVNHomeFragment : Fragment() {

    private lateinit var binding: FragmentLinkBVNHomeBinding
    private lateinit var viewModel: LinkBVNViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_link_b_v_n_home, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LinkBVNViewModel::class.java)

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_linkBVNHomeFragment_to_BVNVerificationFragment)
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }
}