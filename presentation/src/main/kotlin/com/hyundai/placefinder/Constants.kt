package com.hyundai.placefinder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

const val PLAY_MAP_APP_KEY = "bTVEMm5hNzBGQjB6azczTzhYeXk6SEVhaTFya1kzUkM2V3R4RDM3Sk4="

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