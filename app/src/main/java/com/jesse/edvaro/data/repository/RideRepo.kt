package com.jesse.edvaro.data.repository

import com.jesse.edvaro.data.network.Ride

interface RideRepo{
    suspend fun getRides(): List<Ride>
}