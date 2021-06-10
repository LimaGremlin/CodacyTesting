package com.wayapaychat.ng.auth.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.presentation.model.Pages

class CustomHomePageAdapter(private  val context: Context):PagerAdapter() {

    private var pagesList: List<Pages> = listOf()

    fun setDataList(list: List<Pages>) { pagesList = list }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = pagesList.size

    fun getPageCount(): Int = pagesList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pg = pagesList[position]
        val view  = LayoutInflater.from(context).inflate(R.layout.pager_layout, container, false)
        val imageView = view.findViewById<ImageView>(R.id.pager_image)
        val textView =view.findViewById<TextView>(R.id.pager_text)
        val title = view.findViewById<TextView>(R.id.pager_title)
        imageView.setImageResource(pg.image)
        textView.text = pg.description
        title.text = pg.title
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}