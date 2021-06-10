package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentLandingBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingFragment : Fragment(), View.OnClickListener {

    val binding: FragmentLandingBinding by lazy {
        FragmentLandingBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthenticationActivityViewModel by navGraphViewModels(R.id.main_nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setupListener()
        setupObservers()
//        viewModel.getAllMoment()
        return binding.root
    }

    private fun setupObservers() {
//        viewModel.momentsResult.observe(viewLifecycleOwner, momentsObserver)
    }

    private fun setupListener() {
        binding.makePostBtn.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.makePostBtn -> {
//                findNavController().navigate(Uri.parse("wayapay://wayagram/create-options"))
            }
        }
    }

//    private val momentsObserver = Observer<MomentResponse> {
//        it?.let {
//            val adapter = AllMomentsRecyclerAdapter()
//            adapter.submitList(it.data)
//            binding.momentList.adapter = adapter
//        }
//    }
}
