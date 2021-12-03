package com.hyundai.placefinder.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.hyundai.placefinder.applicationScope
import com.hyundai.placefinder.ui.main.PoiResultListAdapter
import com.playmap.sdk.extension.PlayMapPoiItem
import kotlinx.coroutines.launch

@BindingAdapter("bind:items")
fun setItems(view: RecyclerView, items: PagingData<PlayMapPoiItem>?) {
    applicationScope.launch {
        (view.adapter as PoiResultListAdapter).run {
            submitData(items ?: PagingData.empty())
        }
    }
}

@BindingAdapter("bind:text")
fun setText(view: TextView, text: String?) {
    view.text = text
}