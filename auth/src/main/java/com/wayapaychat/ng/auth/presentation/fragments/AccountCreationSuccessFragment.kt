package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentAccountCreationSuccessBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AccountCreationSuccessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountCreationSuccessFragment : Fragment() {

    private lateinit var binding: FragmentAccountCreationSuccessBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_account_creation_success, container, false)

        binding.backToLogInButton.setOnClickListener {
            findNavController().navigate(R.id.action_accountCreationSuccessFragment_to_loginnFragment)
        }

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }
}