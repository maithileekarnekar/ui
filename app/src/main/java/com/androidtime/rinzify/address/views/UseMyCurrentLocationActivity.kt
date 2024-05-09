// UseMyCurrentLocationActivity.kt

package com.androidtime.rinzify.address.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.androidtime.rinzify.R
import com.androidtime.rinzify.databinding.ActivityUseMyCurrentLocationBinding
import com.androidtime.rinzify.homescreen.activities.HomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class UseMyCurrentLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityUseMyCurrentLocationBinding
    private var map: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    interface OnAddressSelectedListener {
        fun onAddressSelected(address: String?, resource: Int)
    }

    var onAddressSelectedListener: OnAddressSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUseMyCurrentLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize toolbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set navigation icon click listener
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        // Initialize listeners
        initListeners()
    }

    private fun initListeners() {
        setupMapClickListener()
        setupButtonListeners()
    }

    private fun setupMapClickListener() {
        map?.setOnMapClickListener { latLng ->
            getAddressFromCoordinates(latLng.latitude, latLng.longitude)
        }
    }

    private fun setupButtonListeners() {
        binding.btnAddLocation.setOnClickListener {
            val address = binding.txtMainAddressTitle.text.toString()
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("address", address)
            }
            startActivity(intent)
        }

        binding.btnHome.setOnClickListener {
            val address = binding.txtMainAddressTitle.text.toString()
            onAddressSelectedListener?.onAddressSelected(address, 1)
            saveAddress(address, 1)
        }

        binding.btnWork.setOnClickListener {
            val address = binding.txtMainAddressTitle.text.toString()
            onAddressSelectedListener?.onAddressSelected(address, 3)
            saveAddress(address, 3)
        }

        binding.btnOther.setOnClickListener {
            val address = binding.txtMainAddressTitle.text.toString()
            onAddressSelectedListener?.onAddressSelected(address, 2)
            saveAddress(address, 2)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initListeners()
        currentLocation(map!!)
    }

    private fun currentLocation(map: GoogleMap) {
        if (hasLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    addMarkerAtCurrentLocation(currentLatLng, map)
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun addMarkerAtCurrentLocation(latLng: LatLng, map: GoogleMap) {
        val marker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Current Location")
        )

        if (marker != null) {
            moveMap(marker, map)
        }
        binding.addressLayout.visibility = View.VISIBLE
        getAddressFromCoordinates(latLng.latitude, latLng.longitude)
    }

    private fun moveMap(marker: Marker, map: GoogleMap) {
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                marker.position,
                20F,
                90F,
                65F
            )
        )

        map.animateCamera(
            cameraUpdate,
            1500,
            object : GoogleMap.CancelableCallback {
                override fun onCancel() {
                }

                override fun onFinish() {
                }
            }
        )
    }

    private fun projection() {
        val latLng = map?.projection?.fromScreenLocation(Point(300, 300))
        if (latLng != null) {
            map?.projection?.toScreenLocation(latLng)
        }
    }

    private fun getAddressFromCoordinates(latitude: Double, longitude: Double) {
        lifecycleScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(this@UseMyCurrentLocationActivity)
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    if (address != null) {
                        initAddressListener(address)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun initAddressListener(address: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            val parts = address.split(", ")
            val mainCity = parts[parts.size - 3]
            val cityAddress = parts.subList(1, parts.size).joinToString(", ")
            binding.txtDetailAddress.text = cityAddress
            binding.txtMainAddressTitle.text = mainCity

            binding.btnHome.setBackgroundColor(resources.getColor(R.color.app_accent_color))
            binding.btnWork.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnOther.setBackgroundColor(resources.getColor(R.color.white))

            binding.btnAddLocation.visibility = View.VISIBLE
            binding.btnHome.visibility = View.VISIBLE
            binding.btnWork.visibility = View.VISIBLE
            binding.btnOther.visibility = View.VISIBLE
        }
    }

    private fun saveAddress(address: String, type: Int) {
        binding.txtMainAddressTitle.text = ""
        binding.txtDetailAddress.text = ""

        binding.txtMainAddressTitle.visibility = View.GONE
        binding.txtDetailAddress.visibility = View.GONE
        binding.btnAddLocation.visibility = View.VISIBLE

        binding.btnHome.visibility = View.VISIBLE
        binding.btnWork.visibility = View.VISIBLE
        binding.btnOther.visibility = View.VISIBLE
    }



    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val DEFAULT_ZOOM = 15f
    }
}