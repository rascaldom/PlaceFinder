package com.hyundai.data.datasource

import com.hyundai.data.local.dao.PlaceDao
import com.hyundai.data.model.GroupEntity
import com.hyundai.data.model.PlaceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocalDataSourceImpl(private val placeDao: PlaceDao) : LocalDataSource {

    override fun getGroups(): Flow<List<GroupEntity>> {
        return flow {
            emit(placeDao.selectGroup())
        }.flowOn(Dispatchers.IO)
    }

    override fun addGroup(group: GroupEntity): Flow<Long> {
        return flow {
            emit(placeDao.insertGroup(group))
        }.flowOn(Dispatchers.IO)
    }

    override fun deleteGroup(group: GroupEntity) {
        placeDao.deleteGroup(group)
    }

    override fun updateGroup(groupId: Long, time: Long) {
        placeDao.updateGroup(groupId, time)
    }

    override fun addPlace(place: PlaceEntity) {
        placeDao.insertPlace(place)
    }

    override fun getPlaces(groupId: Long): Flow<List<PlaceEntity>> {
        return flow {
            emit(placeDao.selectPlace(groupId))
        }.flowOn(Dispatchers.IO)
    }

    override fun deletePlace(place: PlaceEntity) {
        placeDao.deletePlace(place)
    }

    override fun getAllPlaces(): Flow<List<PlaceEntity>> {
        return flow {
            emit(placeDao.selectAllPlaces())
        }.flowOn(Dispatchers.IO)
    }

}