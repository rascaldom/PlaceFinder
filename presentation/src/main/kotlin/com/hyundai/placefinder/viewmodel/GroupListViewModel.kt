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

    // 같은 내용의 변수를 MutableLiveData 와 LiveData 2개로 나눴습니다.
    // LiveData 프로퍼티를 위와 같이 노출 하는 것은 ViewModel 내부에서만 변경 가능하게 제약을 주기 위해서 입니다.
    // 이렇게 하면 View 에서 ViewModel 의 데이터상태를 변경하지 못하게 됩니다
    private val _groupInfo = MutableLiveData<List<GroupModel>>()
    // mutableLiveData 를 immutable 하게 노출
    // ViewModel 내부에서는 Mutable 한 데이터를 외부에서는 Immutable 하게 사용하도록 제약을 주기 위함
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