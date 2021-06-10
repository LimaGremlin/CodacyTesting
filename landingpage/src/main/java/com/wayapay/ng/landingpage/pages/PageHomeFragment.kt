package com.wayapay.ng.landingpage.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentPageHomeBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [PageHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageHomeFragment : Fragment() {

    private lateinit var binding: FragmentPageHomeBinding
    private lateinit var viewModel: PagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_page_home, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PagesViewModel::class.java)

        //get all followers of a page
        viewModel.getPageFollowers()

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }
}