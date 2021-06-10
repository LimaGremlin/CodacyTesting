package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentAuthHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AuthHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthHomeFragment : Fragment() {

    private lateinit var binding: FragmentAuthHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_auth_home, container, false)

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_authHomeFragment_to_loginnFragment)
        }

        binding.gettingStarted.setOnClickListener {
            findNavController().navigate(R.id.action_authHomeFragment_to_signUpFragment)
        }

        return binding.root
    }
}