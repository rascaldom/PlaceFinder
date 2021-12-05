package com.hyundai.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.hyundai.domain.model.PlaceModel

@Entity(
    tableName = "tb_place",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = CASCADE
        )
    ]
)
data class PlaceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val entity_id: Int,

    @ColumnInfo(name = "group_id")
    val entity_groupId: Long,

    @ColumnInfo(name = "group_title")
    val entity_groupTitle: String,

    @ColumnInfo(name = "title")
    val entity_title: String,

    @ColumnInfo(name = "addr")
    val entity_addr: String,

    @ColumnInfo(name = "center_lat")
    val entity_centerLat: Double,

    @ColumnInfo(name = "center_lon")
    val entity_centerLon: Double,

    @ColumnInfo(name = "created_at")
    val entity_createdAt: Long

) : PlaceModel {
    override val id: Int
        get() = entity_id

    override val groupId: Long
        get() = entity_groupId

    override val groupTitle: String
        get() = entity_groupTitle

    override val title: String
        get() = entity_title

    override val addr: String
        get() = entity_addr

    override val centerLat: Double
        get() = entity_centerLat

    override val centerLon: Double
        get() = entity_centerLon

}
