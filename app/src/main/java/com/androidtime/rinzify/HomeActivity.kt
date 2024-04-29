package com.androidtime.rinzify

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.androidtime.rinzify.commons.adapter.ImageSliderAdapter
import com.androidtime.rinzify.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding
    private lateinit var viewPager: ViewPager
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var imageList: List<Int>
    private var currentPage = 0
    private val DELAY_MS: Long = 3000 // Delay in milliseconds
    private val PERIOD_MS: Long = 5000 // Period in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        viewPager = activityHomeBinding.viewPagerforImages // Use the class-level variable

        val tabLayout = findViewById<TabLayout>(R.id.selectedImageIndicator)
        tabLayout.setupWithViewPager(viewPager)

        imageList = listOf(
            R.drawable.advertisement1,
            R.drawable.advertisement2,
            R.drawable.advertisement3
        )
        imageSliderAdapter = ImageSliderAdapter(this, imageList)
        viewPager.adapter = imageSliderAdapter

        // Start auto image slider
        autoImageSlider()
    }

    private fun autoImageSlider() {
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (currentPage == imageList.size) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        handler.postDelayed(update, DELAY_MS)
    }
}
