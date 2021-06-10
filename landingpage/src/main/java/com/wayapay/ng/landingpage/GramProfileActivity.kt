package com.wayapay.ng.landingpage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ActivityGramProfileBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.profile.*
import com.wayapaychat.core.adapters.MyFragmentPagerAdapter
import com.wayapaychat.local.room.models.WayaGramUser


class GramProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGramProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var fragmentPagerAdapter: MyFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gram_profile)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        val bundle = intent.extras
        if(bundle != null){
            val ownerProfile = bundle.getSerializable(IntentBundles.PROFILE_KEY) as WayaGramUser
            viewModel.setOwnerProfile(ownerProfile)
            viewModel.getOwnerProfileByID(ownerProfile.authId.toIntOrNull() ?: -1)
            val userId = bundle.getString(IntentBundles.USER_ID_KEY) as String
            viewModel.setUserProfileId(userId)
            viewModel.setIsOwner(ownerProfile.id == userId)
            viewModel.getUserPost(ownerProfile.id)
            viewModel.getUserPages(ownerProfile.id)
            viewModel.getUserGroups(ownerProfile.id)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white));
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(android.R.color.transparent));
        fragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, RecyclerView.HORIZONTAL)
        setUpViewPager()

        viewModel.getOwnerProfile().observe(this, Observer {
            binding.collapsingToolBar.title = it.displayName
        })

        binding.profileButton.setOnClickListener {
            profileButtonClicked()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    fun profileButtonClicked(){
        val user = viewModel.getOwnerProfile().value ?: WayaGramUser()
        val isOwner = viewModel.getIsOwner().value ?: false
        if(isOwner){
            val intent = Intent(this, EditGramProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpViewPager(){
        fragmentPagerAdapter.clearFragment()
        fragmentPagerAdapter.addFragment(TimelineFragment(), getString(R.string.timeline))
        fragmentPagerAdapter.addFragment(ProfileFollowFragment(), getString(R.string.follow))
        fragmentPagerAdapter.addFragment(ProfileGroupFragment(), getString(R.string.groups))
        fragmentPagerAdapter.addFragment(ProfilePagesFragment(), getString(R.string.pages))
        binding.viewpager.adapter = fragmentPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    override fun onResume() {
        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)
        viewModel.getGramUserAsync(id.toString())
        //call this to update user profile
        viewModel.getUserProfileById(id)
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}