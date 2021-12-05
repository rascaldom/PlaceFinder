package com.hyundai.placefinder.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hyundai.placefinder.databinding.ItemFindPoiResultBinding
import com.playmap.sdk.extension.PlayMapPoiItem

class PoiResultListAdapter : PagingDataAdapter<PlayMapPoiItem, PoiResultListAdapter.ItemViewHolder>(Companion) {

    var onItemClick: ((Int) -> Unit)? = null
    var onAddButtonClick: ((PlayMapPoiItem?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemFindPoiResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind()
    }

    inner class ItemViewHolder(private val binding: ItemFindPoiResultBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                root.setOnClickListener {
                    onItemClick?.invoke(bindingAdapterPosition)
                }
                binding.add.setOnClickListener {
                    onAddButtonClick?.invoke(getItem(bindingAdapterPosition))
                }
            }
        }

        fun bind() {
            with(binding) {
                data = getItem(bindingAdapterPosition)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<PlayMapPoiItem>() {
        override fun areItemsTheSame(oldItem: PlayMapPoiItem, newItem: PlayMapPoiItem): Boolean {
            return oldItem.poiId == newItem.poiId
        }

        override fun areContentsTheSame(oldItem: PlayMapPoiItem, newItem: PlayMapPoiItem): Boolean {
            return oldItem.centerLat == newItem.centerLat
        }
    }

}