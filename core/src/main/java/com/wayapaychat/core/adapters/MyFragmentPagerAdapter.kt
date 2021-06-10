package com.wayapaychat.core.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    var listFragments = mutableListOf<Fragment>()
    var listFragmentTitle = mutableListOf<String>()

    override fun getItem(position: Int): Fragment = listFragments[position]

    override fun getCount(): Int = listFragments.size

    override fun getPageTitle(position: Int): CharSequence? = listFragmentTitle[position]

    fun addFragment(fragment: Fragment, title: String){
        listFragmentTitle.add(title)
        listFragments.add(fragment)
    }

    fun clearFragment(){
        listFragments.clear()
        listFragmentTitle.clear()
        notifyDataSetChanged()
    }
}