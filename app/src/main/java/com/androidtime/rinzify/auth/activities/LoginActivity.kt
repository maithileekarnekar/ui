package com.androidtime.rinzify.auth.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidtime.rinzify.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        initListeners()
    }


    private fun initListeners() {

        activityLoginBinding.btnNext.setOnClickListener {
            val intent = Intent(this, OTPVerificationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}