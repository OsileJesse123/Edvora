package com.jesse.edvaro.data.repository


import android.util.Log
import com.jesse.edvaro.data.model.Ride
import com.jesse.edvaro.data.model.User
import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.di.DefaultDispatcher
import com.jesse.edvaro.di.IODispatcher
import com.jesse.edvaro.utils.RideData
import com.jesse.edvaro.utils.formatRideDate
import kotlinx.coroutines.*
import javax.inject.Inject


class RideRepository @Inject constructor(
    private val rideService: RideService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val userRepo: UserRepo
    ): RideRepo {

    var user = User(0, "", "")

    override suspend fun getRides(): List<Ride> {
        return try {
            coroutineScope{
                user = userRepo.getUser()
                Log.e("RideRepo", "StationCode: ${user.stationCode}")

                val rides = withContext(ioDispatcher){
                    rideService.getRides()
                }
                launch(defaultDispatcher) {
                    rides.forEach {
                        it.date = formatRideDate(it.date)
                    }

                }.join()

                withContext(defaultDispatcher){
                    RideData.getNearestStation(
                        user.stationCode
                        , rides)
                }
                //rides
            }
        } catch (e: Exception){
            listOf()
        }
    }
}