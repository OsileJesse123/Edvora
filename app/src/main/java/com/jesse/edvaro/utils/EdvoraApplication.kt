package com.jesse.edvaro.utils

import android.app.Application
//import com.jesse.edvaro.data.network.RideApi
import com.jesse.edvaro.data.repository.RideRepository
import com.jesse.edvaro.data.repository.UserRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EdvoraApplication: Application() {
//    val userRepo get() = UserRepository(RideApi.retrofitService)
//    val rideRepo get() = RideRepository(RideApi.retrofitService)
}