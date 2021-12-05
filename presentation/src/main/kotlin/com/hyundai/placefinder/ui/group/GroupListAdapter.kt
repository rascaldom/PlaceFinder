package com.hyundai.placefinder.ui.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hyundai.domain.model.GroupModel
import com.hyundai.placefinder.databinding.ItemGroupListBinding

class GroupListAdapter : ListAdapter<GroupModel, GroupListAdapter.ItemViewHolder>(Companion) {

    var onItemClick: ((GroupModel?) -> Unit)? = null
    var onDeleteButtonClick: ((GroupModel?) -> Unit)? = null

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: super.getItemId(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListAdapter.ItemViewHolder {
        return ItemViewHolder(ItemGroupListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GroupListAdapter.ItemViewHolder, position: Int) {
        holder.bind()
    }

    inner class ItemViewHolder(val binding: ItemGroupListBinding) : RecyclerView.ViewHolder(binding.root) {
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

    companion object : DiffUtil.ItemCallback<GroupModel>() {
        override fun areItemsTheSame(oldItem: GroupModel, newItem: GroupModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GroupModel, newItem: GroupModel): Boolean {
            return oldItem.modifiedAt == newItem.modifiedAt
        }
    }

}