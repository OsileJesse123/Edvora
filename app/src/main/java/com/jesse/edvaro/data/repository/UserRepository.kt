package com.jesse.edvaro.data.repository

import com.jesse.edvaro.data.model.User
import com.jesse.edvaro.data.network.RideService
import javax.inject.Inject

class UserRepository @Inject constructor(private val rideService: RideService): UserRepo {
    override suspend fun getUser(): User {
        return try{
            rideService.getUser()
        }catch(e: Exception){
            User(0, "", "")
        }
    }
}