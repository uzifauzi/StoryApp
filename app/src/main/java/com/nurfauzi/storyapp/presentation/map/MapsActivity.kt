package com.nurfauzi.storyapp.presentation.map

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.nurfauzi.storyapp.R
import com.nurfauzi.storyapp.data.StoryResult
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.data.preferences.ViewModelFactory
import com.nurfauzi.storyapp.databinding.ActivityMapsBinding
import com.nurfauzi.storyapp.domain.User

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = LoginPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this,pref)
        mapsViewModel = ViewModelProvider(this,factory).get(
            MapsViewModel::class.java
        )

        mapsViewModel.getToken().observe(this) {
            user = it
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()

        mapsViewModel.getAllStoryLocation(1,user.token).observe(this) { result ->
            when(result) {
                is StoryResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        val dataStories = result.data
                        for(i in dataStories) {
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(i.lat,i.lon))
                                    .title(i.name)
                                    .snippet(i.description)
                                    .alpha(0.8f)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(i.lat,i.lon), 15f))
                        }
                    }
                }
                is StoryResult.Loading -> {
                }
                is StoryResult.Error -> {
                    Toast.makeText(this, "Marker gagal dimuat.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        const val TAG = "maps_activity"
        const val EXTRA_TOKEN = "extra_token"
    }
}