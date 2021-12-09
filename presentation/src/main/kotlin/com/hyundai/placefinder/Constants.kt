package com.hyundai.placefinder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

const val PLAY_MAP_APP_KEY = "bTVEMm5hNzBGQjB6azczTzhYeXk6SEVhaTFya1kzUkM2V3R4RDM3Sk4="

/* launch : coroutine 빌더, CoroutineScope 필요
    launch 블럭은 job Type을 리턴한다.
    job은 실행중인 코루틴을 취소할 수 있다.
    job.cancel()+job.join()은 job.cancelAndJoin()
 */

// runBlocking : coroutine 빌더, 해당 thread block

// SupervisorJob의 경우 아래 방향으로만 취소를 전파시킵니다
/*
supervisorScope
    SupervisorScope 자체에서 Exception이 발생하면 자식 Coroutine을 모두 취소시킨후 부모로 Exception을 전달 시킨다.
    SupervisorScope는 param으로 Handler를 설정할 수 없기 때문이다.
    SupervisorScope의 자식 coroutine에서 Exception이 발생하는경우 자식 스스로 Exception을 처리해야 한다.
        -자식이 Handler를 달든..자식 내부에서 try-catch를 하든...
        -이는 부모로 실패를 전달하지 않는 특성에 기인한다.
        -만약 자식이 Handler 없이 exception을 일으키면 외부에서는 try-catch로는 이 exception을 handling 할 수 없다!!!
        -외부에서 CoroutineExceptionHandler를 이용해서 처리해야 한다.
여러개의 coroutine builder 중첩되어 안쪽에서 excpetion이 발생하는 경우 handler는 가장 바깥쪽에 위치해야만 exception을 전달받을 수 있다.
 */
val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

const val POI_PAGING_SIZE = 20

object ParameterType {
    const val TYPE_KEYWORD = "keyword"
    const val TYPE_LATITUDE = "latitude"
    const val TYPE_LONGITUDE = "longitude"
}

object PoiSortType {
    const val TYPE_RECOMMEND = 1
    const val TYPE_NAME = 2
    const val TYPE_DISTANCE = 3
}

object PagingExceptionType {
    const val TYPE_NO_RESULT = "NO_RESULT"
}