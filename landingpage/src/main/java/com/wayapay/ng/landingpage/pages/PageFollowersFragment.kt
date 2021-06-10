package com.wayapay.ng.landingpage.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayapay.ng.landingpage.R

/**
 * A simple [Fragment] subclass.
 * Use the [PageFollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageFollowersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_followers, container, false)
    }
}