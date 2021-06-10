package com.wayapaychat.ng.auth.presentation.fragments.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentResetPinSuccessBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPinSuccessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPinSuccessFragment : Fragment() {

    private lateinit var binding: FragmentResetPinSuccessBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_reset_pin_success, container, false)

        binding.backToLogInButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }
}