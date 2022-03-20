package com.jesse.edvaro.data.repository


import com.jesse.edvaro.data.network.Ride
import com.jesse.edvaro.data.network.RideData
import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.utils.RideState
import com.jesse.edvaro.utils.UserCurrentDate
import com.jesse.edvaro.utils.formatRideDate
import com.jesse.edvaro.utils.isRideUpcomingOrPastOrCurrent

class RideRepository(private val rideService: RideService): RideRepo {

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