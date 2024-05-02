package com.androidtime.rinzify.homescreen.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.androidtime.rinzify.R
import com.androidtime.rinzify.commons.adapter.ImageSliderAdapter
import com.androidtime.rinzify.databinding.ActivityHomeBinding
import com.androidtime.rinzify.homescreen.adapters.ReviewsAdpater
import com.androidtime.rinzify.homescreen.models.Review
import com.google.android.material.tabs.TabLayout
import java.util.Timer
import java.util.TimerTask


class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding
    private var reviewsList: ArrayList<Review> = ArrayList()
    private lateinit var reviewsAdpater: ReviewsAdpater
    private lateinit var viewPager: ViewPager
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var imageList: List<Int>
    private var currentPage = 0
    private val DELAY_MS: Long = 3000
    private val PERIOD_MS: Long = 3000
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        viewPager = activityHomeBinding.viewPagerforImages // Use the class-level variable

        val tabLayout = findViewById<TabLayout>(R.id.selectedImageIndicator)
        tabLayout.setupWithViewPager(viewPager)

        imageList = listOf(
            R.drawable.advertisement1,
            R.drawable.ads2,
            R.drawable.advertisement1
        )
        imageSliderAdapter = ImageSliderAdapter(this, imageList)
        viewPager.adapter = imageSliderAdapter

        checkLocationPermission()
        autoImageSlider()
        initAdapter()
        initDatatoReviews()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Location permission is already granted, proceed with your logic
            // ...
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Location permission granted, proceed with your logic
                    // ...
                } else {
                    Toast.makeText(
                        this,
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun initAdapter() {
        activityHomeBinding.recyclerReviewsView.layoutManager =
            LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL, false
            )

        reviewsAdpater = ReviewsAdpater(reviewsList)
        activityHomeBinding.recyclerReviewsView.adapter = reviewsAdpater
    }

    private fun initDatatoReviews() {
        reviewsList.add(
            Review(
                1,
                5,
                "Excellent Service! Very impressed with the thorough car wash. My car looks brand new!",
                "Mitesh Shetty, Bangalore"
            )
        )
        reviewsList.add(
            Review(
                2,
                5,
                "Highly Recommend! Great value for money. Friendly staff and excellent service. Will definitely use them again!",
                "Ketan Desai, Mumbai"
            )
        )
        reviewsList.add(
            Review(
                3,
                5,
                "Happy Customer, The car wash was quick and efficient. They did a great job on the interior cleaning as well!",
                "Milan Sharma, Delhi"
            )
        )
    }

    private fun autoImageSlider() {
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (currentPage == imageList.size) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}
