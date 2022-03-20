package com.jesse.edvaro.data.repository

import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.data.network.User

class UserRepository(private val rideService: RideService): UserRepo {
    override suspend fun getUser(): User {
        return try{
            rideService.getUser()
        }catch(e: Exception){
            User(0, "", "")
        }
    }
}