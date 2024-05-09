package com.androidtime.rinzify.auth.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import com.androidtime.rinzify.R
import com.androidtime.rinzify.auth.activities.models.UserCredentials
import com.androidtime.rinzify.databinding.ActivityOtpVerficationBinding
import com.androidtime.rinzify.homescreen.activities.HomeActivity

class OTPVerificationActivity : AppCompatActivity() {
    private lateinit var otpVerficationActivityBinding: ActivityOtpVerficationBinding
    private lateinit var userCredentials: UserCredentials

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        otpVerficationActivityBinding = ActivityOtpVerficationBinding.inflate(layoutInflater)
        setContentView(otpVerficationActivityBinding.root)

        // Retrieve user credentials from intent extra
        userCredentials = intent.getSerializableExtra("user_credentials") as UserCredentials

        // Display mobile number and country code
        otpVerficationActivityBinding.txtPhoneNumber.text = "${userCredentials.countryCode} ${userCredentials.mobileNumber}"


        // Retrieve OTP from intent extras
        val otp = intent.getStringExtra("otp")

        // Autofill OTP
        if (!otp.isNullOrEmpty()) {
            autofillOTP(otp)
        }
        initListeners()
    }

    private fun initListeners() {
        otpVerficationActivityBinding.btnNext.setOnClickListener {
            val enteredOTP = getEnteredOTP()
            if (isValidOTP(enteredOTP)) {
                navigateToHomeActivity()
            } else {
                Toast.makeText(this, "Invalid OTP, please try again", Toast.LENGTH_SHORT).show()
            }
        }

        otpVerficationActivityBinding.txtResendOTP.setOnClickListener {
            resendOTP()
        }
    }

    private fun resendOTP() {
        // Generate OTP
        val otp = generateOTP()

        // Display OTP notification
        generateAndDisplayOTPNotification(otp)

        autofillOTP(otp)
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
    private fun generateAndDisplayOTPNotification(otp: String) {
        val notificationBuilder = NotificationCompat.Builder(this, "OTP_CHANNEL")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("OTP Received")
            .setContentText("Your OTP is $otp")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(0, notificationBuilder.build())
        }
    }
    private fun autofillOTP(otp: String) {
            otpVerficationActivityBinding.edtOTP1.postDelayed({
                otpVerficationActivityBinding.edtOTP1.setText(otp[0].toString())
                otpVerficationActivityBinding.edtOTP2.postDelayed({
                    otpVerficationActivityBinding.edtOTP2.setText(otp[1].toString())
                    otpVerficationActivityBinding.edtOTP3.postDelayed({
                        otpVerficationActivityBinding.edtOTP3.setText(otp[2].toString())
                        otpVerficationActivityBinding.edtOTP4.postDelayed({
                            otpVerficationActivityBinding.edtOTP4.setText(otp[3].toString())
                        }, 200)
                    }, 200)
                }, 200)
            }, 200)
    }

    private fun getEnteredOTP(): String {
        val otp1 = otpVerficationActivityBinding.edtOTP1.text.toString()
        val otp2 = otpVerficationActivityBinding.edtOTP2.text.toString()
        val otp3 = otpVerficationActivityBinding.edtOTP3.text.toString()
        val otp4 = otpVerficationActivityBinding.edtOTP4.text.toString()
        return "$otp1$otp2$otp3$otp4"
    }

    private fun isValidOTP(otp: String): Boolean {
        return otp.isNotEmpty() && otp.length == 4
    }
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
