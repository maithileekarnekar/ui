package com.androidtime.rinzify.auth.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.androidtime.rinzify.R
import com.androidtime.rinzify.auth.activities.models.UserCredentials
import com.androidtime.rinzify.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var userCredentials: UserCredentials

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        initListeners()

        // Create Notification Channel (uncomment for Android Oreo+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    private fun initListeners() {
        activityLoginBinding.btnNext.setOnClickListener {
            val countryCode = activityLoginBinding.countryCode.selectedCountryCode
            val mobileNumber = activityLoginBinding.edtMobileNumber.text.toString()

            if (mobileNumber.isEmpty()) {
                // Display a toast message indicating that the mobile number is required
                Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show()
            } else {
                // Create user credentials object
                userCredentials = UserCredentials(countryCode, mobileNumber)

                // Trigger OTP notification
                generateAndDisplayOTPNotification()
            }
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "OTP Notification Channel"
            val descriptionText = "Channel for displaying OTP notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("OTP_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun generateOTP(): String {
        val otpLength = 4
        val otp = StringBuilder()

        repeat(otpLength) {
            val digit = (0..9).random()
            otp.append(digit)
        }

        return otp.toString()
    }


    @SuppressLint("MissingPermission")
    private fun generateAndDisplayOTPNotification() {
        // Generate OTP
        val otp = generateOTP()

        val notificationBuilder = NotificationCompat.Builder(this, "OTP_CHANNEL")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("OTP Received")
            .setContentText("Your OTP is $otp")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, notificationBuilder.build())
        }

        // Navigate to OTPVerificationActivity
        navigateToOTPVerificationScreen(userCredentials, otp)
    }

    private fun navigateToOTPVerificationScreen(userCredentials: UserCredentials, otp: String) {
        // Here you can start the OTPVerificationActivity and pass the userCredentials and the OTP
        val intent = Intent(this, OTPVerificationActivity::class.java)
        intent.putExtra("user_credentials", userCredentials)
        intent.putExtra("otp", otp)
        startActivity(intent)
        finish()
    }

}
