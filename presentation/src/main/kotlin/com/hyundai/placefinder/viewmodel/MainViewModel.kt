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


/*
KoinComponent를 사용한 접근 방식을 필드 주입이라고 합니다. 다음은 생성자 주입이 더 나은 몇 가지 이유입니다.
1. 더 단순함: 클래스는 누가 만족하는지가 아니라 필요한 종속성에 대해서만 관심을 갖습니다.
2. 더 강력함: 필드 주입이 완료된 후가 아니라 생성되는 즉시 일관된 상태의 클래스 인스턴스입니다.
3. 더 쉬운 테스트: 생성자에서 종속성을 선언하면 테스트하는 동안 모의 객체를 더 쉽게 제공할 수 있습니다.
 */
class MainViewModel(
    private val groupUseCase: GroupUseCase,
    private val placeUseCase: PlaceUseCase
) : ViewModel() {

    private val mapRestApi: PlayMapRestApi by lazy { PlayMapRestApi() }

    private val queryLiveData = MutableLiveData<Map<String, String>>()

    /* Dispatchers
    코루틴 실행에 사용하는 스레드를 결정한다. 모든 코루틴 빌더(ex, launch, async) 는 디스패처를 지정할 수 있다. 다음은 대표적인 Dispathers
        Default: 오래 걸리는 작업을 할 때. 공유된 백그라운드 스레드 풀 사용한다.
        IO: 파일을 쓰거나 API 콜 같은 상황에서 사용한다.
        Main: 메인스레드 작업에 사용한다.
 */

    // 하나의 데이터가 바뀔 때마다 다른 여러 데이터들도 함께 바껴야하는 상황
    // 지연 평가(Lazy Estimation)로 동작하기 때문에 원천이 되는 객체의 변화가 일어나지 않는다면 동작하지 않습니다

    // MediatorLiveData를 간단히 설명하자면 Rx의 merge 함수와 비슷하다.
    // 서로다른 data source(여기서는 LiveData)가 독립적으로 존재하는 상황에서,
    // 각각의 데이터 소스들이 변경되는 것을 따로 따로 관찰하는 것이 아니라 어떤 소스에서 변경이 일어나든 한번에 관찰하려고 하는 것이다

    // map의 경우 람다함수의 return값이 각 요소의 값들을 변경시키는 것에 불과하며 자동으로 LiveData가 되어서 결과물이 반환되었지만,
    // switchMap의 경우 실제로 LiveData 하나를 반환해야 한다.
    // 그래서 switchMap은 Model단이나 Room데이터베이스와 같이 애초에 LiveData를 반환하는 기능들과 자주 함께 쓰인다.
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