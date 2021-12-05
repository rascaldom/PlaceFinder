package com.hyundai.data.repository

import com.hyundai.data.datasource.LocalDataSource
import com.hyundai.data.model.GroupEntity
import com.hyundai.data.model.PlaceEntity
import com.hyundai.domain.model.GroupModel
import com.hyundai.domain.model.PlaceModel
import com.hyundai.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(private val localDataSource: LocalDataSource) : Repository {

    override fun getGroupInfo(): Flow<List<GroupModel>> = localDataSource.getGroups()

    override fun addGroup(title: String) : Flow<Long> {
        val currentTimeMillis = System.currentTimeMillis()
        GroupEntity(currentTimeMillis, title, currentTimeMillis, currentTimeMillis).apply {
            return localDataSource.addGroup(this)
        }
    }

    override fun deleteGroup(groupData: GroupModel) {
        GroupEntity(
            groupData.id,
            groupData.title,
            groupData.createdAt,
            groupData.modifiedAt
        ).apply {
            localDataSource.deleteGroup(this)
        }
    }

    override fun updateGroup(groupId: Long) {
        localDataSource.updateGroup(groupId, System.currentTimeMillis())
    }

    override fun addPlace(placeData: PlaceModel) {
        makePlaceEntity(placeData).apply {
            localDataSource.addPlace(this)
        }
    }

    override fun getPlaceInfo(groupId: Long): Flow<List<PlaceModel>> = localDataSource.getPlaces(groupId)

    override fun deletePlace(placeData: PlaceModel) {
        makePlaceEntity(placeData).apply {
            localDataSource.deletePlace(this)
        }
    }

    private fun makePlaceEntity(placeData: PlaceModel) : PlaceEntity =
        PlaceEntity(
            placeData.id,
            placeData.groupId,
            placeData.groupTitle,
            placeData.title,
            placeData.addr,
            placeData.centerLat,
            placeData.centerLon,
            System.currentTimeMillis()
        )

    override fun getAllPlaces(): Flow<List<PlaceModel>> = localDataSource.getAllPlaces()

}