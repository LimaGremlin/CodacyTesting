package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentChooseLandingBinding

class ChooseLandingFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentChooseLandingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_landing,
            container, false)

        setupListener()
        return binding.root
    }

    private fun setupListener() {
        binding.connect.setOnClickListener(this)
        binding.discover.setOnClickListener(this)
        binding.transact.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.connect -> {
//                Prefs.putString(Constants.INTEREST, "connect")
                navigateToRecommended("connect")
            }
            binding.discover -> {
//                Prefs.putString(Constants.INTEREST, "discover")
                navigateToRecommended("discover")
            }
            binding.transact -> {
//                Prefs.putString(Constants.INTEREST, "transact")
                navigateToRecommended("transact")
            }
        }
    }

    private fun navigateToRecommended(interest: String){
        val action = ChooseLandingFragmentDirections.actionChooseLandingFragmentToRecommendedFragment().setInterest(interest)
        Navigation.findNavController(binding.discover).navigate(action)
    }
}
