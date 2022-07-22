package com.jesse.edvaro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "uesr_table")
data class User(
    @Json(name = "station_code")
    val stationCode: Int,
    @PrimaryKey
    val name: String,
    val url: String
)

data class StationCodeToRide(
    val code: Int,
    val ride: Ride
)

@Entity(tableName = "ride_table")
data class Ride(
    val city: String,
    var date: String,
    @Json(name = "destination_station_code")
    val destinationStationCode: Int,
    @PrimaryKey
    val id: Int,
    @Json(name = "map_url")
    val mapUrl: String,
    @Json(name = "origin_station_code")
    val originStationCode: Int,
    val state: String,
    @Json(name = "station_path")
    val stationPath: List<Int>
)