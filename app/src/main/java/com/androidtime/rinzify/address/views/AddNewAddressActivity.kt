package com.androidtime.rinzify.address.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidtime.rinzify.databinding.ActivityAddAddressBinding
import com.androidtime.rinzify.databinding.ActivityAddNewAddressBinding

class AddNewAddressActivity : AppCompatActivity() {

    private lateinit var activityAddNewAddressBinding: ActivityAddNewAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddNewAddressBinding = ActivityAddNewAddressBinding.inflate(layoutInflater)
        setContentView(activityAddNewAddressBinding.root)

        initListeners()
    }
    private fun initListeners() {

    }
}