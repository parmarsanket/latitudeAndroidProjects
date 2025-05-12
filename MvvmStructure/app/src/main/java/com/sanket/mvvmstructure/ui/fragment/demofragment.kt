package com.sanket.mvvmstructure.ui.fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sanket.mvvmstructure.R

class demofragment:Fragment() , OnMapReadyCallback{
    private lateinit var googleMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.demo, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)

        val bottomSheet = view.findViewById<NestedScrollView>(R.id.bottomSheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)

// Make it half the screen height
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        behavior.peekHeight = screenHeight / 2

// Optional: disable hide state
        behavior.isHideable = false

// Start in collapsed (half screen) state
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Do whatever you want with the map
        val sampleLocation = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sampleLocation).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 10f))
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap=googleMap
//        val sampleLocation = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sampleLocation).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 10f))
//
//    }
}