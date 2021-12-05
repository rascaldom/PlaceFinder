package com.hyundai.placefinder.viewmodel

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hyundai.domain.model.GroupModel
import com.hyundai.domain.model.PlaceModel
import com.hyundai.domain.usecase.GroupUseCase
import com.hyundai.domain.usecase.PlaceUseCase
import com.hyundai.placefinder.POI_PAGING_SIZE
import com.hyundai.placefinder.ParameterType
import com.hyundai.placefinder.paging.PoiPagingSource
import com.playmap.sdk.PlayMapPoint
import com.playmap.sdk.extension.PlayMapPoiItem
import com.playmap.sdk.network.PlayMapRestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class MainViewModel(
    private val groupUseCase: GroupUseCase,
    private val placeUseCase: PlaceUseCase
) : ViewModel() {

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

    fun getGroupInfo(): Flow<List<GroupModel>> = groupUseCase.getGroupInfo()

    fun addGroup(title: String): Flow<Long> {
        return groupUseCase.addGroup(title)
    }

    fun addGroupWithGroupId(groupId: Long, data: PlayMapPoiItem) {
        viewModelScope.launch {
            groupUseCase.getGroupInfo().collect { list ->
                list.forEach {
                    if (it.id == groupId) {
                        addPlace(it, data)
                    }
                }
            }
        }
    }

    fun addPlace(groupData: GroupModel, data: PlayMapPoiItem) {
        val placeData = object : PlaceModel {
            override val id: Int get() = data.poiId
            override val groupId: Long get() = groupData.id
            override val groupTitle: String get() = groupData.title
            override val title: String get() = data.title
            override val addr: String get() = data.addr
            override val centerLat: Double get() = data.centerLat
            override val centerLon: Double get() = data.centerLon
        }

        viewModelScope.launch(Dispatchers.IO) {
            placeUseCase.addPlace(placeData)
        }
    }

    fun updateGroup(groupId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            groupUseCase.updateGroup(groupId)
        }
    }

}