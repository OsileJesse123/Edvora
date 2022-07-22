package com.jesse.edvaro.data.repository


import com.jesse.edvaro.data.model.Ride
import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.utils.formatRideDate
import javax.inject.Inject


class RideRepository @Inject constructor(private val rideService: RideService): RideRepo {

    override suspend fun getRides(): List<Ride> {
        return try {
            val rides = rideService.getRides()
            rides.forEach {
                it.date = formatRideDate(it.date)
            }
            rides
        } catch (e: Exception){
            listOf()
        }
    }
}