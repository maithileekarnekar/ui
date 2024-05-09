package com.androidtime.rinzify.address.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.androidtime.rinzify.R
import com.androidtime.rinzify.databinding.ActivityAddNewAddressBinding
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

class UseMyCurrentLocationActivity : AppCompatActivity(){
    private lateinit var activityUseMyCurrentLocationBinding: ActivityUseMyCurrentLocationBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUseMyCurrentLocationBinding = ActivityUseMyCurrentLocationBinding.inflate(layoutInflater)
        setContentView(activityUseMyCurrentLocationBinding.root)

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(MapReadyCallback())

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapClickListener { latLng ->
                getAddressFromCoordinates(latLng.latitude, latLng.longitude)
            }
        }

        // Initialize listeners
        initListeners()
    }

    private fun initListeners() {
        // Locate me button click listener
        activityUseMyCurrentLocationBinding.txtLocateMe.setOnClickListener {
            getCurrentLocation()
        }

        // Change location button click listener
        activityUseMyCurrentLocationBinding.btnChangeLocation.setOnClickListener {
            val intent = Intent(this, AddNewAddressActivity::class.java)
            startActivity(intent)
        }

        activityUseMyCurrentLocationBinding.btnAddLocation.setOnClickListener {
            val address = activityUseMyCurrentLocationBinding.txtMainAddressTitle.text.toString()

            // Navigate to HomeActivity with the updated address
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("address", address)
            }
            startActivity(intent)
        }
    }

    private fun initMapSettings(googleMap: GoogleMap) {
         //Configure map settings here
                 initMapSettings(googleMap)

                // Check location permissions
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }

                // Enable my location on the map
                googleMap.isMyLocationEnabled = true

                // Get and update current location on the map
                getCurrentLocation()
    }

    private fun initListener() {


        googleMap.setOnMapClickListener { position ->
            projection()
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("sample marker")
            )

            if (marker != null) {
                moveMap(marker, googleMap)
            }

        }

    }

    inner class MapReadyCallback : OnMapReadyCallback {
        override fun onMapReady(googleMap: GoogleMap) {

            googleMap.setOnMapClickListener { latLng ->
                getAddressFromCoordinates(latLng.latitude, latLng.longitude)
            }
            currentLocation(googleMap)
        }
    }


    private fun currentLocation(map: GoogleMap) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                addMarkerAtCurrentLocation(currentLatLng, map)
            }
        }
    }


    private fun addMarkerAtCurrentLocation(latLng: LatLng, map: GoogleMap) {

        val mapPos = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("current location")
        )

        if (mapPos != null) {
            moveMap(mapPos, map)
        }
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

        val latLng = googleMap.projection.fromScreenLocation(Point(300, 300))
        googleMap.projection.toScreenLocation(latLng)

    }


    private fun getAddressFromCoordinates(latitude: Double, longitude: Double) {
        lifecycleScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(this@UseMyCurrentLocationActivity)
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        val address = addresses.get(0)?.getAddressLine(0)
                        initAddressListener(address)

                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun initAddressListener(address: String?) {

        lifecycleScope.launch(Dispatchers.Main) {
            val parts = address?.split(", ")
            val mainCity  = if (parts!= null) {
                parts[parts.size - 3]
            }else{null}
            val cityAddress = parts?.subList(1, parts.size)?.joinToString(", ") ?: ""
//            binding.txtLongAddress.text = cityAddress
//            binding.txtAddress.text = mainCity
//
//            val home = 1
//            val other = 2
//            val work = 3
//
//            binding.btnHome.setOnClickListener {
//                binding.btnHome.setBackgroundColor(resources.getColor(R.color.accent))
//
//                binding.btnAddLocation.setOnClickListener {
//                    onAddressSelectedListener?.onAddressSelected(cityAddress, home)
//                    parentFragmentManager.popBackStack()
//                }
//            }
//            binding.btnWork.setOnClickListener {
//                binding.btnWork.setBackgroundColor(resources.getColor(R.color.accent))
//
//                binding.btnAddLocation.setOnClickListener {
//                    onAddressSelectedListener?.onAddressSelected(cityAddress, work)
//                    parentFragmentManager.popBackStack()
//                }
//
//            }
//            binding.btnOther.setOnClickListener {
//                binding.btnOther.setBackgroundColor(resources.getColor(R.color.accent))
//
//                binding.btnAddLocation.setOnClickListener {
//                    onAddressSelectedListener?.onAddressSelected(cityAddress, other)
//                    parentFragmentManager.popBackStack()
//                }
//
//            }
//        }


    }
}

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude

                // Move the camera to the user's location
                val currentLatLng = LatLng(latitude, longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))

                // Get the address from the location
                getAddressFromCoordinates(latitude, longitude)
            } else {
                Toast.makeText(this, "Unable to get your location", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun getAddressFromCoordinates(latitude: Double, longitude: Double) {
//        val geocoder = Geocoder(this)
//        try {
//            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
//            if (addresses != null && addresses.isNotEmpty()) {
//                val address = addresses[0]
//                val mainAddress = address.subLocality ?: "" + ", " + address.locality ?: ""
//                val detailAddress = address.thoroughfare ?: "" + ", " + address.subThoroughfare ?: ""
//
//                // Update UI with address details
//                activityUseMyCurrentLocationBinding.txtMainAddressTitle.text = mainAddress
//                activityUseMyCurrentLocationBinding.txtDetailAddress.text = detailAddress
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val DEFAULT_ZOOM = 15f
    }
}
