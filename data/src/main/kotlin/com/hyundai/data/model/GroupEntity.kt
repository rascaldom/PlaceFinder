package com.hyundai.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hyundai.domain.model.GroupModel

@Entity(tableName = "tb_group")
data class GroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val entity_id: Long,

    @ColumnInfo(name = "title")
    val entity_title: String,

    @ColumnInfo(name = "created_at")
    val entity_createdAt: Long,

    @ColumnInfo(name = "modified_at")
    val entity_modifiedAt: Long

) : GroupModel {
    override val id: Long
        get() = entity_id

    override val title: String
        get() = entity_title

    override val createdAt: Long
        get() = entity_createdAt

    override val modifiedAt: Long
        get() = entity_modifiedAt

}