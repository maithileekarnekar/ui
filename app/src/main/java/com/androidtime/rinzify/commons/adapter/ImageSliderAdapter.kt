package com.androidtime.rinzify.commons.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.androidtime.rinzify.R
import com.bumptech.glide.Glide

class ImageSliderAdapter(
    private val context: Context,
    private val imageList: List<Int>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.image_list, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imgAdd1)
        Glide.with(context)
            .load(imageList[position])
            .into(imageView)

        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
