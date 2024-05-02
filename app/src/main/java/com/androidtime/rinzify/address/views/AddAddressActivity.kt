package com.androidtime.rinzify.address.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidtime.rinzify.databinding.ActivityAddAddressBinding

class AddAddressActivity : AppCompatActivity() {

    private lateinit var activityAddAddressBinding: ActivityAddAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddAddressBinding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(activityAddAddressBinding.root)

        initListeners()
    }
    private fun initListeners() {
        activityAddAddressBinding.currentLocationLayout.setOnClickListener {
            val intent = Intent(this, UseMyCurrentLocationActivity::class.java)
            startActivity(intent)
        }

        activityAddAddressBinding.addNewAddressesLayout.setOnClickListener {
            val intent = Intent(this, AddNewAddressActivity::class.java)
            startActivity(intent)
        }
    }
}