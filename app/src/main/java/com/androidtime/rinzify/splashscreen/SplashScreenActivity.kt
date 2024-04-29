package com.androidtime.rinzify.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.androidtime.rinzify.MainActivity
import com.androidtime.rinzify.R
import com.androidtime.rinzify.auth.activities.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashScreenLayout: View
    private lateinit var icon1: View
    private lateinit var icon2: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        // Find the splash screen layout and icons
        splashScreenLayout = findViewById(R.id.splash_screen_layout)
        icon1 = findViewById(R.id.icon1)
        icon2 = findViewById(R.id.icon2)

        // Start fade-in animation for the layout
        //splashScreenLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        // Scale and slide left animation for icon1 (growing)
        val scaleAndSlideLeftGrow = AnimationUtils.loadAnimation(this, R.anim.scale_and_slide_left_grow)
        scaleAndSlideLeftGrow.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                // Slide and fade out animation (to the right, longer duration)
                val slideAndFadeOutLeft = AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.slide_and_fade_out_left)
                icon1.startAnimation(slideAndFadeOutLeft)
                slideAndFadeOutLeft.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        icon1.visibility = View.INVISIBLE // Make icon1 invisible after animation
                        icon2.visibility = View.VISIBLE
                        // Optional animation for icon2 appearance (delay slightly)
                        Handler(Looper.getMainLooper()).postDelayed({
                            icon2.startAnimation(AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.slide_in_right))
                        }, 400) // Adjust delay as needed
                        navigateToLoginActivity()
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }

            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
        })
        icon1.startAnimation(scaleAndSlideLeftGrow)
    }



    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
