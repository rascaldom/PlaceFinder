package com.hyundai.placefinder.viewmodel

import androidx.lifecycle.*
import com.hyundai.domain.model.GroupModel
import com.hyundai.domain.model.PlaceModel
import com.hyundai.domain.usecase.GroupUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GroupListViewModel(private val groupUseCase: GroupUseCase) : ViewModel() {

    private val _groupInfo = MutableLiveData<List<GroupModel>>()

    val groupInfo: LiveData<List<GroupModel>> get() = _groupInfo

    fun getGroupInfo() {
        viewModelScope.launch {
            groupUseCase.getGroupInfo().collect {
                _groupInfo.value = it
            }
        }
    }

    fun addGroup(title: String) {
        viewModelScope.launch {
            groupUseCase.addGroup(title).collect()
        }
    }

    fun deleteGroup(groupData: GroupModel) {
        viewModelScope.launch(Dispatchers.IO) {
            groupUseCase.deleteGroup(groupData)
        }
    }

    fun getAllPlaces(): Flow<List<PlaceModel>> = groupUseCase.getAllPlaces()

}