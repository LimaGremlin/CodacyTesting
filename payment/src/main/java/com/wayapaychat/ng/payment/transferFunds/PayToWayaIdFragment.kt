package com.wayapaychat.ng.payment.transferFunds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayapaychat.ng.payment.R


/**
 * A simple [Fragment] subclass.
 * Use the [PayToWayaIdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PayToWayaIdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay_to_waya_id, container, false)
    }


}