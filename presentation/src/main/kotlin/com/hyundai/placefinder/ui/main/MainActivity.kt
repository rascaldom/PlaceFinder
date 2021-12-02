package com.hyundai.placefinder.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.hyundai.placefinder.PLAY_MAP_APP_KEY
import com.hyundai.placefinder.R
import com.hyundai.placefinder.common.BaseActivity
import com.hyundai.placefinder.databinding.ActivityMainBinding
import com.hyundai.placefinder.viewmodel.MainViewModel
import com.playmap.sdk.PlayMapView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val viewModel: MainViewModel by viewModel()

    private val mapView: PlayMapView by lazy { binding.mapView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }

        // GPS 권한 획득
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }

        initializeView()
    }

    private fun initializeView() {
        initializeMapView()
        initializeSearchBar()
    }

    private fun initializeMapView() {
        mapView.setAppkey(PLAY_MAP_APP_KEY, object : PlayMapView.OnApiKeyListenerCallback {
            override fun onApiKeySucceed() {
                lifecycleScope.launch {
                    mapViewCreate()
                }
            }

            override fun onApiKeyFailed(errorMsg: String?) {
                Toast.makeText(this@MainActivity, "Map Loading Success !!!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun mapViewCreate() {
        mapView.initMap(37.56640, 126.97851, 17);

        mapView.onMapInitListener { zoom, playMapPoint ->
            Toast.makeText(this, "Map Loading Success !!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeSearchBar() {
        with (binding.svSearchBar) {
            setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.findAllPoi(query ?: "", mapView.mapCenterPoint)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

}