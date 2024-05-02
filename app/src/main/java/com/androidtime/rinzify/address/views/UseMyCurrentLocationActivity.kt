package com.androidtime.rinzify.address.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidtime.rinzify.R
import com.androidtime.rinzify.databinding.ActivityUseMyCurrentLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class UseMyCurrentLocationActivity : AppCompatActivity() , OnMapReadyCallback {
    private lateinit var activityUseMyCurrentLocationBinding: ActivityUseMyCurrentLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUseMyCurrentLocationBinding = ActivityUseMyCurrentLocationBinding.inflate(layoutInflater)
        setContentView(activityUseMyCurrentLocationBinding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val latitude = 37.7749
        val longitude = -122.4194
        val zoomLevel = 10f
        val location = LatLng(latitude, longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
    }
}