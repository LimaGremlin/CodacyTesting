package com.wayapay.ng.landingpage.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.GramProfileActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.adapter.UserAdapter
import com.wayapay.ng.landingpage.databinding.FragmentSearchUserBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapaychat.local.room.models.WayaGramUser

/**
 * A simple [Fragment] subclass.
 * Use the [SearchUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchUserFragment : Fragment(), UserAdapter.OnClickListener {

    private lateinit var binding: FragmentSearchUserBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search_user, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)

        userAdapter = UserAdapter(this, "")
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = userAdapter
        }

        viewModel.getListSearchedUsers().observe(requireActivity(), Observer {
            userAdapter.swapList(it)
        })

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onProfileClicked(position: Int, tag: String) {
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        val profileOwner = userAdapter.getUser(position)
        val intent = Intent(requireActivity(), GramProfileActivity::class.java)
        intent.putExtra(IntentBundles.PROFILE_KEY, profileOwner)
        intent.putExtra(IntentBundles.USER_ID_KEY, user.id)
        startActivity(intent)
    }
}