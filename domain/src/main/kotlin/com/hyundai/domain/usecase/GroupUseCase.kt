package com.hyundai.domain.usecase

import com.hyundai.domain.model.GroupModel
import com.hyundai.domain.model.PlaceModel
import com.hyundai.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GroupUseCase(private val repository: Repository) {

    fun getGroupInfo(): Flow<List<GroupModel>> = repository.getGroupInfo()

    fun addGroup(title: String): Flow<Long> = repository.addGroup(title)

    fun deleteGroup(groupData: GroupModel) = repository.deleteGroup(groupData)

    fun updateGroup(groupId: Long) = repository.updateGroup(groupId)

    fun getAllPlaces(): Flow<List<PlaceModel>> = repository.getAllPlaces()

}