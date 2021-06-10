package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayapaychat.ng.auth.R

/**
 * A simple [Fragment] subclass.
 * Use the [RegMerchantPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegMerchantPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reg_merchant_password, container, false)
    }
}