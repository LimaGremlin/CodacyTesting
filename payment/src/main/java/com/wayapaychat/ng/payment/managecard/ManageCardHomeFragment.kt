package com.wayapaychat.ng.payment.managecard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentManageCardHomeBinding
import com.wayapaychat.ng.payment.linkbvn.LinkBVNViewModel


class ManageCardHomeFragment : Fragment() {

    private lateinit var binding: FragmentManageCardHomeBinding
    private lateinit var viewModel: ManageCardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_manage_card_home, container, false)

        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ManageCardViewModel::class.java)

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel


        viewModel.getListCards().observe(requireActivity(), Observer {
            if (it.isNullOrEmpty()) {
                binding.emptyContainer.visibility = View.VISIBLE
            } else {
                binding.notEmpty.visibility = View.VISIBLE
                binding.rvCards.layoutManager = LinearLayoutManager(requireActivity())
                binding.rvCards.adapter = CardsAdapter(it)
            }
        })

        binding.addCardButton.setOnClickListener{
            findNavController().navigate(R.id.action_manageCardHomeFragment_to_chooseWalletFragment2)

        }

        return binding.root
    }


}