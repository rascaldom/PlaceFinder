package com.hyundai.placefinder.viewmodel

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hyundai.domain.usecase.GetPlaceGroupListUseCase
import com.hyundai.placefinder.POI_PAGING_SIZE
import com.hyundai.placefinder.ParameterType
import com.hyundai.placefinder.paging.PoiPagingSource
import com.playmap.sdk.PlayMapPoint
import com.playmap.sdk.extension.PlayMapPoiItem
import com.playmap.sdk.network.PlayMapRestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn


class MainViewModel(private val getPlaceGroupListUseCase: GetPlaceGroupListUseCase) : ViewModel() {

    private val mapRestApi: PlayMapRestApi by lazy { PlayMapRestApi() }

    private val queryLiveData = MutableLiveData<Map<String, String>>()

    val poiResult: LiveData<PagingData<PlayMapPoiItem>> = queryLiveData.switchMap {
        Pager(
            config = PagingConfig(POI_PAGING_SIZE),
            pagingSourceFactory = {
                PoiPagingSource(mapRestApi, it) }
        ).flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope).asLiveData()
    }

    fun findAllPoi(keyword: String, center: PlayMapPoint) {
        queryLiveData.value = mapOf(
            ParameterType.TYPE_KEYWORD to keyword,
            ParameterType.TYPE_LATITUDE to center.latitude.toString(),
            ParameterType.TYPE_LONGITUDE to center.longitude.toString()
        )
    }

}