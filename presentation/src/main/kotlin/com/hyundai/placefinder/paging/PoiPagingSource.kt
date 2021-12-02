package com.hyundai.placefinder.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hyundai.placefinder.POI_PAGING_SIZE
import com.hyundai.placefinder.PagingExceptionType
import com.hyundai.placefinder.ParameterType
import com.hyundai.placefinder.PoiSortType
import com.playmap.sdk.extension.PlayMapPoiItem
import com.playmap.sdk.network.PlayMapRestApi

class PoiPagingSource(private val mapRestApi: PlayMapRestApi, private val queries: Map<String, String>) : PagingSource<Int, PlayMapPoiItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlayMapPoiItem> {
        println("[rascaldom] 11111111111111111111111111111111")
        return try {
            println("[rascaldom] 555555555555555555555555")
            val page = params.key ?: 1
            val response = mapRestApi.findAllPOI(
                queries[ParameterType.TYPE_KEYWORD],
                queries[ParameterType.TYPE_LATITUDE]?.toDouble() ?: 0.0,
                queries[ParameterType.TYPE_LONGITUDE]?.toDouble() ?: 0.0,
                PoiSortType.TYPE_DISTANCE,
                "",
                0,
                POI_PAGING_SIZE
            )
            if (!response.isNullOrEmpty()) {
                response.forEach {
                    println("[rascaldom] findAllPoi() poiId : ${it.poiId}")
                    println("[rascaldom] findAllPoi() title : ${it.title}")
                    println("[rascaldom] findAllPoi() addr : ${it.addr}")
                    println("[rascaldom] findAllPoi() centerLat : ${it.centerLat}")
                    println("[rascaldom] findAllPoi() centerLon : ${it.centerLon}")
                }
                LoadResult.Page(
                    data = response,
                    prevKey = null,
                    nextKey = if (response.size < 20) null else page + 1
                )
            } else {
                throw Exception(PagingExceptionType.TYPE_NO_RESULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PlayMapPoiItem>): Int? {
        println("[rascaldom] 333333333333333333333333333333")
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}