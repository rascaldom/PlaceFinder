package com.hyundai.data.datasource

import com.hyundai.data.model.GroupEntity
import com.hyundai.data.model.PlaceEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getGroups(): Flow<List<GroupEntity>>

    fun addGroup(group: GroupEntity): Flow<Long>

    fun deleteGroup(group: GroupEntity)

    fun updateGroup(groupId: Long, time: Long)

    fun addPlace(place: PlaceEntity)

    fun getPlaces(groupId: Long): Flow<List<PlaceEntity>>

    fun deletePlace(place:PlaceEntity)

    fun getAllPlaces(): Flow<List<PlaceEntity>>

}