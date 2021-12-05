package com.hyundai.placefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyundai.domain.model.PlaceModel
import com.hyundai.domain.usecase.GroupUseCase
import com.hyundai.domain.usecase.PlaceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaceListViewModel(
    private val groupUseCase: GroupUseCase,
    private val placeUseCase: PlaceUseCase
) : ViewModel() {

    private val _placeInfo = MutableLiveData<List<PlaceModel>>()

    val placeInfo: LiveData<List<PlaceModel>> get() = _placeInfo

    fun getPlaceInfo(groupId: Long) {
        viewModelScope.launch {
            placeUseCase.getPlaceInfo(groupId).collect {
                _placeInfo.value = it
            }
        }
    }

    fun deletePlace(placeData: PlaceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            placeUseCase.deletePlace(placeData)
        }
    }

    fun updateGroup(groupId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            groupUseCase.updateGroup(groupId)
        }
    }
}