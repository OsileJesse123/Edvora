package com.jesse.edvaro.data.repository

import com.jesse.edvaro.data.network.User

interface UserRepo {
    suspend fun getUser(): User
}