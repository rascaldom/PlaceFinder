package com.hyundai.domain.usecase

import com.hyundai.domain.model.PlaceModel
import com.hyundai.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class PlaceUseCase(private val repository: Repository) {

    fun addPlace(placeData: PlaceModel) = repository.addPlace(placeData)

    fun getPlaceInfo(groupId: Long): Flow<List<PlaceModel>> = repository.getPlaceInfo(groupId)

    fun deletePlace(placeData: PlaceModel) = repository.deletePlace(placeData)

}