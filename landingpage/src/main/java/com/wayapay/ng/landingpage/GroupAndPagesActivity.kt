package com.wayapay.ng.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ActivityGroupAndPagesBinding
import com.wayapay.ng.landingpage.groupandpages.GroupFragment
import com.wayapay.ng.landingpage.groupandpages.GroupPagesViewModel
import com.wayapay.ng.landingpage.groupandpages.PagesFragment
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapaychat.core.adapters.MyFragmentPagerAdapter
import com.wayapaychat.local.room.models.WayaGramUser

class GroupAndPagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupAndPagesBinding
    private lateinit var viewModel: GroupPagesViewModel
    private lateinit var fragmentPagerAdapter: MyFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_and_pages)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GroupPagesViewModel::class.java)

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        //check if intent came with a bundle
        val bundle = intent.extras
        if(bundle != null){
            val user = bundle.getSerializable(IntentBundles.GRAM_PROFILE_KEY) as WayaGramUser
            viewModel.setUser(user)
            viewModel.getUserPages(user.id)
            viewModel.getAllUserPages(user.id, 1)
            viewModel.getUserGroups(user.id)
            viewModel.getJoinedGroups(user.id)
        }

        fragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, RecyclerView.HORIZONTAL)
        fragmentPagerAdapter.clearFragment()
        fragmentPagerAdapter.addFragment(GroupFragment(), getString(R.string.groups))
        fragmentPagerAdapter.addFragment(PagesFragment(), getString(R.string.pages))
        binding.viewpager.adapter = fragmentPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}