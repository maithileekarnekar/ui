package com.androidtime.rinzify.auth.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidtime.rinzify.HomeActivity

import com.androidtime.rinzify.databinding.ActivityOtpVerficationBinding

class OTPVerificationActivity : AppCompatActivity() {
    private lateinit var otpVerficationActivityBinding: ActivityOtpVerficationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        otpVerficationActivityBinding = ActivityOtpVerficationBinding.inflate(layoutInflater)
        setContentView(otpVerficationActivityBinding.root)

        initListeners()

    }

    private fun initListeners() {
        otpVerficationActivityBinding.btnNext.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}