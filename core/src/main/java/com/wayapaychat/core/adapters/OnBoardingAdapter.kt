package com.wayapaychat.core.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.wayapaychat.core.R
import com.wayapaychat.core.models.Page

class OnBoardingAdapter(val context: Context): PagerAdapter() {

    private var pageList: List<Page> = listOf()

    fun setDataList(list: List<Page>) {
        this.pageList = list
    }
    override fun isViewFromObject(view: View, dObject: Any): Boolean {
        return view == dObject
    }

    override fun getCount(): Int {
        return pageList.size
    }

    fun getPageCount():Int = pageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pg = pageList[position]
        val view  = LayoutInflater.from(this.context).inflate(R.layout.pager_layout, container, false)
        val imageView = view.findViewById<ImageView>(R.id.pager_image)
        val textView =view.findViewById<TextView>(R.id.pager_text)
        val title = view.findViewById<TextView>(R.id.pager_title)
        imageView.setImageResource(pg.image)
        textView.text = pg.description
        title.text = pg.title
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, dObject: Any) {
        container.removeView(dObject as View?)
    }
}
