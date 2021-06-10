package com.wayapaychat.ng.auth.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.wayapaychat.core.adapters.OnBoardingAdapter
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.models.Page
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.CustomizeLandingPageFragmentBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel

class CustomizeLandingPages :
    BaseFragment<CustomizeLandingPageFragmentBinding, AuthenticationActivityViewModel>(),
    View.OnClickListener {

    lateinit var binding: CustomizeLandingPageFragmentBinding

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.customize_landing_page_fragment

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: CustomizeLandingPageFragmentBinding) {
        this.binding = binding
    }

    private val adapter: OnBoardingAdapter by lazy {
        OnBoardingAdapter(requireContext())
    }

    private var pageLists = ArrayList<Page>()
    private var pageNo = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setData()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSelectWayaPayChatScreen.setOnClickListener(this)
    }

    private fun setData() {
        val page1 = Page(
            R.drawable.dashboard_onne,
            "Wayachat",
            "Chat and call clients, friends and family"
        )
        val page2 = Page(
            R.drawable.dashboard_twwo,
            "Wayagram",
            " Discover beautiful places, people, conversations, vendors and clients"
        )
        val page3 =
            Page(R.drawable.dashboard_thhre, "WayaPay", "Free Internet Banking & Bills Payment")

        pageLists.add(page1)
        pageLists.add(page2)
        pageLists.add(page3)

        adapter.setDataList(pageLists)
        binding.pager.adapter = adapter
        binding.itemTab.setupWithViewPager(binding.pager)


        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (val current = binding.pager.currentItem) {
                    0 -> pageNo = current
                    1 -> pageNo = current
                    2 -> pageNo = current

                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSelectWayaPayChatScreen -> {
                setUpSharedPreference(pageNo)
            }
        }
    }

    private fun setUpSharedPreference(page: Int){
        val sharedPref = requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with (sharedPref.edit()) {
            putInt(getString(R.string.landing_page_key), page)
            apply()
        }
        when(page){
            0 -> {
                startActivity(AppActivityNavCommands.getLandingIntent(requireContext()))
                requireActivity().finish()
            }
            1 -> {
                startActivity(AppActivityNavCommands.getLandingIntent(requireContext()))
                requireActivity().finish()
            }
            2 -> {
                startActivity(AppActivityNavCommands.getPaymentIntent(requireContext()))
                requireActivity().finish()
            }
        }
    }
}
