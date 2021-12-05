package com.hyundai.data.local.dao

import androidx.room.*
import com.hyundai.data.model.GroupEntity
import com.hyundai.data.model.PlaceEntity

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGroup(group: GroupEntity) : Long

    @Delete
    fun deleteGroup(group: GroupEntity)

    @Query("SELECT * FROM tb_group ORDER BY created_at ASC")
    fun selectGroup(): List<GroupEntity>

    @Query("UPDATE TB_GROUP SET modified_at = :time WHERE id = :groupId")
    fun updateGroup(groupId: Long, time: Long)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPlace(place: PlaceEntity)

    @Delete
    fun deletePlace(place: PlaceEntity)

    @Query("SELECT * FROM tb_place WHERE group_id = :groupId ORDER BY created_at ASC")
    fun selectPlace(groupId: Long): List<PlaceEntity>

    @Query("SELECT * FROM tb_place")
    fun selectAllPlaces(): List<PlaceEntity>

}