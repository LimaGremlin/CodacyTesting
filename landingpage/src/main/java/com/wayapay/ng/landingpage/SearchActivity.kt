package com.wayapay.ng.landingpage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ActivitySearchBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.profile.ProfileFollowFragment
import com.wayapay.ng.landingpage.profile.ProfileGroupFragment
import com.wayapay.ng.landingpage.profile.ProfilePagesFragment
import com.wayapay.ng.landingpage.profile.TimelineFragment
import com.wayapay.ng.landingpage.search.*
import com.wayapaychat.core.adapters.MyFragmentPagerAdapter
import com.wayapaychat.core.contracts.InterModuleIntent
import com.wayapaychat.core.utils.hideKeyboard
import com.wayapaychat.local.room.models.WayaGramUser

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var fragmentPagerAdapter: MyFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        fragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, RecyclerView.HORIZONTAL)
        setUpViewPager()

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)
        viewModel.getGramUserAsync(id.toString())

        //check if intent came with a bundle
        val bundle = intent.extras
        if(bundle != null){
            val param = bundle.getString(InterModuleIntent.SEARCH_EXTRA) ?: ""
            val userId = bundle.getString(IntentBundles.USER_ID_KEY) as String
            binding.searchEditText.setText(param)
            viewModel.setUserId(userId)
            viewModel.searchPost(param)
            viewModel.searchUsers(param, userId)
            viewModel.searchGroups(userId, param, 1)
            viewModel.searchPages(param, 1, userId)
        }

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    val userId = viewModel.getUserId().value ?: ""
                    if (!TextUtils.isEmpty(binding.searchEditText.text.toString().trim())) {
                        val param = binding.searchEditText.text.toString()
                        viewModel.searchPost(param)
                        viewModel.searchUsers(param, userId)
                        viewModel.searchGroups(userId, param, 1)
                        viewModel.searchPages(param, 1, userId)
                        hideKeyboard(this)
                    }
                    true
                }
            }
            false
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setUpViewPager(){
        fragmentPagerAdapter.clearFragment()
        fragmentPagerAdapter.addFragment(SearchPostFragment(), getString(R.string.posts))
        fragmentPagerAdapter.addFragment(SearchUserFragment(), getString(R.string.users))
        fragmentPagerAdapter.addFragment(SearchPageFragment(), getString(R.string.pages))
        fragmentPagerAdapter.addFragment(SearchGroupFragment(), getString(R.string.groups))
        binding.viewpager.adapter = fragmentPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }
}