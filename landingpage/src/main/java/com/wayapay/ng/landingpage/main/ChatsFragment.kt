package com.wayapay.ng.landingpage.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.chat.ChatApplication
import com.wayapay.ng.landingpage.databinding.FragmentChatsBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.models.WayaGramUser
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var viewModel: LandingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_chats, container, false)

        val viewModelFactory = LandingViewModelFactory(
            requireActivity(),
            requireActivity().application
        )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LandingViewModel::class.java)

        viewModel.setAddPeopleVisibility(false)
        viewModel.setOptionsVisibility(true)
        viewModel.setDrawerEnabled(true)
        viewModel.setIsBottomNavVisible(true)
        viewModel.setIsHeaderVisible(true)
        viewModel.setFabVisibility(true)
        viewModel.setSearchVisibility(false)
        viewModel.setSearchButtonVisibility(true)
        viewModel.setHeaderText(getString(R.string.wayachat))

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }
}