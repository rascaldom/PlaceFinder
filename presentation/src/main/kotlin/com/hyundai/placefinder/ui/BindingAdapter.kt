package com.hyundai.placefinder.ui

import androidx.databinding.BindingAdapter
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.RecyclerView
import com.playmap.sdk.extension.PlayMapPoiItem

@BindingAdapter("bind:items")
fun setItems(view: RecyclerView, items: PagingData<PlayMapPoiItem>?) {
    println("[rascaldom] 6666666666666666666666666 items : $items")
    items?.map {
        println("[rascaldom] findAllPoi() poiId : ${it.poiId}")
        println("[rascaldom] findAllPoi() title : ${it.title}")
        println("[rascaldom] findAllPoi() addr : ${it.addr}")
        println("[rascaldom] findAllPoi() centerLat : ${it.centerLat}")
        println("[rascaldom] findAllPoi() centerLon : ${it.centerLon}")
    }
//    (view.adapter as SearchListAdapter).apply {
//        setListItems(items ?: emptyList())
//        notifyDataSetChanged()
//    }
}