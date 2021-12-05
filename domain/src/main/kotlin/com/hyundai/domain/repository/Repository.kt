package com.hyundai.domain.repository

import com.hyundai.domain.model.GroupModel
import com.hyundai.domain.model.PlaceModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getGroupInfo(): Flow<List<GroupModel>>

    fun addGroup(title: String) : Flow<Long>

    fun deleteGroup(groupData: GroupModel)

    fun updateGroup(groupId: Long)

    fun addPlace(placeData: PlaceModel)

    fun getPlaceInfo(groupId: Long): Flow<List<PlaceModel>>

    fun deletePlace(placeData: PlaceModel)

    fun getAllPlaces(): Flow<List<PlaceModel>>

}