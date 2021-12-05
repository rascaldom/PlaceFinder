package com.hyundai.domain.model

interface PlaceModel {

    val id: Int
    val groupId: Long
    val groupTitle: String
    val title: String
    val addr: String
    val centerLat: Double
    val centerLon: Double

}