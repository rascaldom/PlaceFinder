package com.hyundai.placefinder.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hyundai.placefinder.POI_PAGING_SIZE
import com.hyundai.placefinder.PagingExceptionType
import com.hyundai.placefinder.ParameterType
import com.hyundai.placefinder.PoiSortType
import com.playmap.sdk.extension.PlayMapPoiItem
import com.playmap.sdk.network.PlayMapRestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PoiPagingSource(private val mapRestApi: PlayMapRestApi, private val queries: Map<String, String>) : PagingSource<Int, PlayMapPoiItem>() {

    // continuation passing style
    // labels
    // suspend 동작 원리
    // 일반함수로 변경하면서 마지막 파라미터에 Continuation(callback) 추가
    // 라벨링 과정을 통해서 라인들을 switch-case 스타일로 변경
    // 각각의 연산이 종료되면 다음 연산 재개를 위해 스테이트머신을(다음 포인트) 참조하고((ex)label++) resume 함수를 호출 (callback)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlayMapPoiItem> {
        return try {
            withContext(Dispatchers.IO) {
                val page = params.key ?: 1
                val response = mapRestApi.findAllPOI(
                    queries[ParameterType.TYPE_KEYWORD],
                    queries[ParameterType.TYPE_LATITUDE]?.toDouble() ?: 0.0,
                    queries[ParameterType.TYPE_LONGITUDE]?.toDouble() ?: 0.0,
                    PoiSortType.TYPE_DISTANCE,
                    "",
                    POI_PAGING_SIZE * (page - 1),
                    POI_PAGING_SIZE
                )
                if (!response.isNullOrEmpty()) {
                    LoadResult.Page(
                        data = response,
                        prevKey = null,
                        nextKey = if (response.size < 20) null else page + 1
                    )
                } else {
                    throw Exception(PagingExceptionType.TYPE_NO_RESULT)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PlayMapPoiItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}