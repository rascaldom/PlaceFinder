package com.hyundai.placefinder.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.hyundai.domain.model.GroupModel
import com.hyundai.placefinder.applicationScope
import com.hyundai.placefinder.ui.group.GroupListAdapter
import com.hyundai.placefinder.ui.main.PoiResultListAdapter
import com.playmap.sdk.extension.PlayMapPoiItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bind:pois")
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

@BindingAdapter("bind:groups")
fun setItems(view: RecyclerView, items: List<GroupModel>?) {
    (view.adapter as GroupListAdapter).run {
        submitList(items)
    }
}

@BindingAdapter("bind:date", "bind:format")
fun setText(view: TextView, millis: Long, format: String) {
    view.text = SimpleDateFormat(format, Locale.KOREA).format(Date(millis))
}