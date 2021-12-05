package com.hyundai.placefinder.ui.place

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyundai.domain.model.PlaceModel
import com.hyundai.placefinder.PLAY_MAP_APP_KEY
import com.hyundai.placefinder.R
import com.hyundai.placefinder.common.BaseActivity
import com.hyundai.placefinder.databinding.ActivityPlaceBinding
import com.hyundai.placefinder.viewmodel.PlaceListViewModel
import com.playmap.sdk.PlayMapPoint
import com.playmap.sdk.PlayMapView
import com.playmap.sdk.shape.PlayMapMarker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceListActivity : BaseActivity() {

    private val binding: ActivityPlaceBinding by binding(R.layout.activity_place)
    private val viewModel: PlaceListViewModel by viewModel()

    private val mapView: PlayMapView by lazy { binding.mapView }

    private val placeListAdapter: PlaceListAdapter by inject()

    private val groupId: Long by lazy { intent.getLongExtra(EXTRA_KEY_GROUP_ID, 0) }
    private val groupTitle: String by lazy { intent.getStringExtra(EXTRA_KEY_GROUP_TITLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@PlaceListActivity
            viewModel = this@PlaceListActivity.viewModel
        }

        initializeView()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    private fun initializeView() {
        binding.tvTitle.text = groupTitle
        initializeMapView()
        initializePlaceListAdapter()
        initializeObserver()
    }

    private fun initializeMapView() {
        mapView.setAppkey(PLAY_MAP_APP_KEY, object : PlayMapView.OnApiKeyListenerCallback {
            override fun onApiKeySucceed() {
                lifecycleScope.launch {
                    mapViewCreate()
                }
            }

            override fun onApiKeyFailed(errorMsg: String?) {
                Toast.makeText(this@PlaceListActivity, "Map Loading Success !!!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun mapViewCreate() {
        with(mapView) {
            initMap(37.56640, 126.97851, 17)
            onMapInitListener { zoom, playMapPoint ->
                viewModel.getPlaceInfo(groupId)
            }
        }
    }

    private fun initializePlaceListAdapter() {
        with(binding.rvPlaceList) {
            adapter = placeListAdapter.apply {
                onItemClick = {
                    it?.let {
                        mapView.setMapCenterPoint(PlayMapPoint(it.centerLat, it.centerLon), 500)
                    }
                }
                onDeleteButtonClick = {
                    it?.let {
                        viewModel.deletePlace(it)
                        viewModel.updateGroup(groupId)
                        lifecycleScope.launch {
                            delay(200)
                            removePoiMarker(it.id)
                            viewModel.getPlaceInfo(groupId)
                        }
                    }
                }
            }
            addItemDecoration(DividerItemDecoration(this@PlaceListActivity, LinearLayoutManager.VERTICAL))
        }
    }

    private fun initializeObserver() {
        viewModel.placeInfo.observe(this, {
            placeListAdapter.submitList(it)
            drawPoiMarker(it)
            if (it.isNotEmpty()) {
                mapView.setMapCenterPoint(PlayMapPoint(it[0].centerLat, it[0].centerLon), 500)
            }
        })
    }

    private fun drawPoiMarker(list: List<PlaceModel>) {
        list.forEach { data ->
            PlayMapMarker().apply {
                mapPoint = PlayMapPoint(data.centerLat, data.centerLon)
                canShowCallout = true
                calloutTitle = data.title
                calloutSubTitle = data.addr
                this@PlaceListActivity.mapView.addMarkerItem(data.id.toString(), this)
            }
        }
    }

    private fun removePoiMarker(id: Int) {
        mapView.removeMarkerItem(id.toString())
    }

    companion object {
        const val EXTRA_KEY_GROUP_ID = "extra_key_group_id"
        const val EXTRA_KEY_GROUP_TITLE = "extra_key_group_title"
    }

}