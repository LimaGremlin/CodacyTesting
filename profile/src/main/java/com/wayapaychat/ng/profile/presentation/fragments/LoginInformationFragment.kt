package com.wayapaychat.ng.profile.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wayapaychat.core.models.LogInInfo
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentLoginInformationBinding
import com.wayapaychat.ng.profile.presentation.adapter.LoginHistoryAdapter
import com.wayapaychat.ng.profile.presentation.viewModels.LoginInformationViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginInformationFragment : Fragment() {

    private lateinit var binding: FragmentLoginInformationBinding

    private  val adapter = LoginHistoryAdapter()
    private var logInlog = mutableListOf<LogInInfo>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login_information, container, false)

        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)

        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
        val model: LoginInformationViewModel by viewModels()


        model.getLoginHistoryAsync(id)
        model.getLogInHistory().observe(requireActivity(), Observer {
            binding.location.text = it.address
            binding.device.text = it.device
            binding.time.text = it.logInDate;
            binding.timeLog.adapter= adapter
            adapter.populateDataset(logInlog)
            binding.progressLayoutVisibility.visibility = View.GONE
        })

        binding.navButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }
}
