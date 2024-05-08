package com.androidtime.rinzify.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.androidtime.rinzify.R
import com.androidtime.rinzify.auth.activities.LoginActivity
import com.androidtime.rinzify.databinding.ActivitySplashscreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var activitySplashScreenBinding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashScreenBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(activitySplashScreenBinding.root)

        // Start the animation sequence
        animateIcon1()
    }

    private fun animateIcon1() {
        val slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
        slideOutLeft.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                activitySplashScreenBinding.icon1.visibility = View.INVISIBLE
                animateIcon2()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
        activitySplashScreenBinding.icon1.startAnimation(slideOutLeft)
    }

    private fun animateIcon2() {
        activitySplashScreenBinding.icon2.visibility = View.VISIBLE
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        slideInLeft.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    activitySplashScreenBinding.icon2.visibility = View.INVISIBLE
                    animateIcon3()
                }, 500)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
        activitySplashScreenBinding.icon2.startAnimation(slideInLeft)
    }


    private fun animateIcon3() {
        // Animation for icon3 appearance
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        activitySplashScreenBinding.icon3.visibility = View.VISIBLE
        activitySplashScreenBinding.icon3.startAnimation(slideInRight)
        // Navigate to the next activity after the animation completes
        slideInRight.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                navigateToLoginActivity()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
