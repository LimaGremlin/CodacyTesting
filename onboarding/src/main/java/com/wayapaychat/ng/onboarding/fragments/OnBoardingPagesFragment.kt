package com.wayapaychat.ng.onboarding.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.wayapaychat.core.adapters.OnBoardingAdapter
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.onboarding.OnBoardingViewModel
import com.wayapaychat.ng.onboarding.R
import com.wayapaychat.ng.onboarding.databinding.FragmentOnBoardingPagesBinding
import com.wayapaychat.ng.onboarding.utils.getListPages

class OnBoardingPagesFragment :
    BaseFragment<FragmentOnBoardingPagesBinding, OnBoardingViewModel>() {

    private lateinit var binding: FragmentOnBoardingPagesBinding
    lateinit var adapter: OnBoardingAdapter

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()

    override fun getViewModel(): OnBoardingViewModel = onBoardingViewModel

    override fun getLayoutId(): Int = R.layout.fragment_on_boarding_pages

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentOnBoardingPagesBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        setOnClickListeners()
        setObservers()
    }

    private fun setObservers() {
        with(onBoardingViewModel) {
            onBoardingStatus.observe(
                viewLifecycleOwner, Observer{
                    when (it) {
                        true -> {
                            println("Something here")
                        }
                        false -> {
                            println("Something there, Hello!!!")
                        }
                    }
                }
            )
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            next.setOnClickListener {
                //Check if current page is last page
                if (slider.currentItem < (adapter.getPageCount() - 2))
                    slider.setCurrentItem((slider.currentItem + 1), true)
                else {
                    onBoardingViewModel.setCompleteOnBoarding()
                    navigate(WayaNavigationCommand.To(OnBoardingPagesFragmentDirections.actionOnBoardingPagesFragmentToOnBoardingLandingFragment()))
                }
            }

            skipSkip.setOnClickListener {
                onBoardingViewModel.setCompleteOnBoarding()
                navigate(WayaNavigationCommand.To(OnBoardingPagesFragmentDirections.actionOnBoardingPagesFragmentToOnBoardingLandingFragment()))
            }
        }
    }

    private fun initViews() {
        //initialize OnBoardingAdapter
        adapter = OnBoardingAdapter(requireContext()).apply {
            //set the list of pages in OnBoardingAdapter
            setDataList(getListPages(requireContext()))
        }

        with(binding) {
            //set slider adapter to OnBoardingAdapter
            slider.adapter = adapter

            //link TabLayout with ViewPager
            itemTab.setupWithViewPager(binding.slider)

            slider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int, positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    //Check if position is in FakePage
                    if (position == (adapter.getPageCount() - 1)) {
                        binding.slider.setCurrentItem(position - 1, false)
                        startActivity(AppActivityNavCommands.getAuthIntent(requireContext()))
                        requireActivity().finish()
                    }
                }
            })
        }
    }
}
