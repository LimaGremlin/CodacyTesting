package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.wayapaychat.core.adapters.OnBoardingAdapter
import com.wayapaychat.core.models.Page
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentCustomizeLandingPageBinding

class CustomizeLandingPages : Fragment(), View.OnClickListener {

    private val binding: FragmentCustomizeLandingPageBinding by lazy {
        FragmentCustomizeLandingPageBinding.inflate(layoutInflater)
    }
    private val adapter: OnBoardingAdapter by lazy {
        OnBoardingAdapter(requireContext())
    }
    private var pageLists = ArrayList<Page>()
    private var pageNo = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setData()
        setupListeners()
        binding.btnSelectWayaPayChatScreen.setOnClickListener(this)
        return binding.root
    }

    private fun setupListeners() {
        binding.btnSelectWayaPayChatScreen.setOnClickListener(this)
    }

    private fun setData() {

        val page1 = Page(R.drawable.ic_connect, "Wayachat", "Chat and call clients, friends and family")
        val page2 = Page(R.drawable.ic_discover, "Wayagram", " Discover beautiful places, people, conversations, vendors and clients")
        val page3 = Page(R.drawable.ic_transact,  "WayaPay", "Free Internet Banking & Bills Payment")

        pageLists.add(page1)
        pageLists.add(page2)
        pageLists.add(page3)



        adapter.setDataList(pageLists)
        binding.pager.adapter = adapter
        binding.itemTab.setupWithViewPager(binding.pager)


        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(val current = binding.pager.currentItem){
                    0 -> pageNo = current
                    1 -> pageNo = current
                    2 -> pageNo = current

                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun navigateToLogin(interest: String) {
        val action = CustomizeLandingPagesDirections.actionCustomizeLandingPagesToRecommendedFragment().setInterest(interest)
        Navigation.findNavController(binding.btnSelectWayaPayChatScreen).navigate(action)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnSelectWayaPayChatScreen ->{
//                Prefs.putBoolean(Constants.FIRST_TIME_CHOOSE_LANDING, true)
                when(pageNo){
                    0 -> {
//                        Prefs.putString(Constants.INTEREST, "connect")
                        navigateToLogin("connect")
                    }
                    1 -> {
//                        Prefs.putString(Constants.INTEREST, "discover")
                        navigateToLogin("discover")
                    }
                    2 -> {
//                        Prefs.putString(Constants.INTEREST, "transact")
                        navigateToLogin("transact")
                    }
                }
            }
        }
    }
}
