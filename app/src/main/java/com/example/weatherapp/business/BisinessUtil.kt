package com.example.weatherapp.business

import com.example.weatherapp.business.model.GeoCodeModel
import com.example.weatherapp.business.room.GeoCodeEntity


fun GeoCodeModel.mapToEntity() = GeoCodeEntity(
    this.name,
    this.local_names,
    this.lat,
    this.lon,
    this.country,
    this.state ?: "",
    this.isFavorite
)

fun GeoCodeEntity.mapToModel() = GeoCodeModel(
    this.name,
    local_names,
    this.lat,
    this.lon,
    this.country,
    this.state,
    this.isFavorite
)