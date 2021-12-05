package com.hyundai.placefinder.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hyundai.domain.model.PlaceModel
import com.hyundai.placefinder.databinding.ItemPlaceListBinding

class PlaceListAdapter : ListAdapter<PlaceModel, PlaceListAdapter.ItemViewHolder>(Companion) {

    var onItemClick: ((PlaceModel?) -> Unit)? = null
    var onDeleteButtonClick: ((PlaceModel?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListAdapter.ItemViewHolder {
        return ItemViewHolder(ItemPlaceListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlaceListAdapter.ItemViewHolder, position: Int) {
        holder.bind()
    }

    inner class ItemViewHolder(val binding: ItemPlaceListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                root.setOnClickListener {
                    onItemClick?.invoke(getItem(bindingAdapterPosition))
                }
                delete.setOnClickListener {
                    onDeleteButtonClick?.invoke(getItem(bindingAdapterPosition))
                }
            }
        }

        fun bind() {
            with(binding) {
                data = getItem(bindingAdapterPosition)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<PlaceModel>() {
        override fun areItemsTheSame(oldItem: PlaceModel, newItem: PlaceModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlaceModel, newItem: PlaceModel): Boolean {
            return oldItem.addr == newItem.addr
        }
    }

}