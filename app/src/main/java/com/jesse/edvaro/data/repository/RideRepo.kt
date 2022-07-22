package com.jesse.edvaro.data.repository

import com.jesse.edvaro.data.model.Ride
import com.jesse.edvaro.data.model.User

interface RideRepo{
    open var user: User
    suspend fun getRides(): List<Ride>
}