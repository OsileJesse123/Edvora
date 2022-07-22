package com.jesse.edvaro.data.repository

import com.jesse.edvaro.data.model.User
import com.jesse.edvaro.data.network.RideService
import com.jesse.edvaro.di.DefaultDispatcher
import com.jesse.edvaro.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val rideService: RideService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
): UserRepo {
    override suspend fun getUser(): User {
        return try{
           withContext(ioDispatcher){
               rideService.getUser()
           }
        }catch(e: Exception){
            User(0, "", "")
        }
    }
}