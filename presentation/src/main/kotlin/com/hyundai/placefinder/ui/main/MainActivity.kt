package com.hyundai.placefinder.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyundai.domain.model.GroupModel
import com.hyundai.placefinder.PLAY_MAP_APP_KEY
import com.hyundai.placefinder.PagingExceptionType
import com.hyundai.placefinder.R
import com.hyundai.placefinder.common.BaseActivity
import com.hyundai.placefinder.databinding.ActivityMainBinding
import com.hyundai.placefinder.ui.group.GroupListActivity
import com.hyundai.placefinder.viewmodel.MainViewModel
import com.playmap.sdk.PlayMapPoint
import com.playmap.sdk.PlayMapView
import com.playmap.sdk.extension.PlayMapPoiItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val viewModel: MainViewModel by viewModel()

    private val mapView: PlayMapView by lazy { binding.mapView }

    private val poiResultListAdapter: PoiResultListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 0)
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
        initializeMapView()
        initializeSearchBar()
        initializeMoveGroupButton()
        initializeResultListAdapter()
    }

    private fun initializeMapView() {
        mapView.setAppkey(PLAY_MAP_APP_KEY, object : PlayMapView.OnApiKeyListenerCallback {
            override fun onApiKeySucceed() {
                lifecycleScope.launch {
                    mapViewCreate()
                }
            }

            override fun onApiKeyFailed(errorMsg: String?) {

            }

        })
    }

    private fun mapViewCreate() {
        with(mapView) {
            initMap(37.56640, 126.97851, 17)
            onMapInitListener { zoom, playMapPoint ->
                Toast.makeText(this@MainActivity, "Map Loading Success !!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeSearchBar() {
        with(binding.svSearchBar) {
            setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.findAllPoi(query ?: "", mapView.mapCenterPoint)
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun initializeMoveGroupButton() {
        binding.fabMoveGroup.setOnClickListener {
            startActivity(Intent(this@MainActivity, GroupListActivity::class.java))
        }
    }

    private fun initializeResultListAdapter() {
        with(binding.rvResultList) {
            adapter = poiResultListAdapter.apply {
                addLoadStateListener {
                    setLoadState(it)
                }
                onItemClick = { position ->
                    snapshot()[position]?.let {
                        mapView.setMapCenterPoint(PlayMapPoint(it.centerLat, it.centerLon), 500)
                    }
                }
                onAddButtonClick = { placeData ->
                    lifecycleScope.launch {
                        viewModel.getGroupInfo().collect { groupList ->
                            placeData?.let {
                                showGroupListDialog(groupList, it)
                            }
                        }
                    }
                }
            }
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
        }
    }

    private fun showGroupListDialog(groupList: List<GroupModel>, placeData: PlayMapPoiItem) {
        val titleList = ArrayAdapter<String>(this, android.R.layout.select_dialog_item).apply {
            add(getString(R.string.new_group))
            groupList.forEach {
                if (!it.title.isNullOrEmpty()) {
                    add(it.title)
                }
            }
        }
        AlertDialog.Builder(this).apply {
            setTitle(R.string.select_group)
            setAdapter(titleList) { _, id ->
                if (id == 0) {
                    val input = AppCompatEditText(this@MainActivity).apply {
                        setPadding(paddingLeft + 30, paddingTop, paddingRight + 30, paddingBottom)
                    }
                    AlertDialog.Builder(this@MainActivity).apply {
                        setTitle(R.string.new_group)
                        setView(input)
                        setPositiveButton(getString(R.string.confirm)) { _, _ ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                viewModel.addGroup(input.text.toString()).collect {
                                    delay(200)
                                    viewModel.addGroupWithGroupId(it, placeData)
                                }
                            }
                        }
                    }.show()
                } else {
                    addPlace(groupList[id - 1], placeData)
                }
            }
        }.show()
    }

    private fun addPlace(groupData: GroupModel, placeData: PlayMapPoiItem) {
        viewModel.addPlace(groupData, placeData)
        viewModel.updateGroup(groupData.id)
    }

    private fun setLoadState(state: CombinedLoadStates) {
        binding.isLoading = when {
            state.append is LoadState.Loading -> true
            state.refresh is LoadState.Loading -> true
            else -> false
        }
        val error = when {
            state.append is LoadState.Error -> state.append as LoadState.Error
            state.refresh is LoadState.Error -> state.refresh as LoadState.Error
            else -> null
        }
        error?.let {
            when (it.error.message) {
                PagingExceptionType.TYPE_NO_RESULT -> {
                    poiResultListAdapter.submitData(lifecycle, PagingData.empty())
                }
            }
            Toast.makeText(this@MainActivity, it.error.message, Toast.LENGTH_SHORT).show()
        }
    }

}