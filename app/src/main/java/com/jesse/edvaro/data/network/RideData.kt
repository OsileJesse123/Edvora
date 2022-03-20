package com.jesse.edvaro.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

data class User(
    @Json(name = "station_code")
    val stationCode: Int,
    val name: String,
    val url: String
)

data class StationCodeToRide(
    val code: Int,
    val ride: Ride
)

object RideData {

    fun getNearestStation(userStation: Int, rides: List<Ride>): List<Ride> {
        val stationCodeToRides = mutableListOf<StationCodeToRide>()
        var count = 0
        val updatedListOfRides = mutableListOf<Ride>()

        rides.forEach{
            r ->
            for(path in r.stationPath){
                if (path >= userStation){
                    val codeToRide= StationCodeToRide(path, r)
                    stationCodeToRides.add(codeToRide)
                    break
                }
            }
        }

        val ridesSize = stationCodeToRides.size

        while(count < ridesSize){
            val newRide = stationCodeToRides.minByOrNull { it.code }
            newRide?.let {
                updatedListOfRides.add(it.ride)
                stationCodeToRides.remove(it)
            }
            count++
        }
        return updatedListOfRides
    }

}

private const val BASE_URL = "https://assessment.api.vweb.app/"

private val moshi by lazy {
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RideService {
    @GET("user")
    suspend fun getUser(): User

    @GET("rides")
    suspend fun getRides(): List<Ride>
}

object RideApi {
    val retrofitService: RideService by lazy {
        retrofit.create(RideService::class.java)
    }
}

data class Ride(
    val city: String,
    var date: String,
    @Json(name = "destination_station_code")
    val destinationStationCode: Int,
    val id: Int,
    @Json(name = "map_url")
    val mapUrl: String,
    @Json(name = "origin_station_code")
    val originStationCode: Int,
    val state: String,
    @Json(name = "station_path")
    val stationPath: List<Int>
)