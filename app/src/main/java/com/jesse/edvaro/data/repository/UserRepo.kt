package com.jesse.edvaro.data.repository

import com.jesse.edvaro.data.model.User

interface UserRepo {
    suspend fun getUser(): User
}