package com.jesse.edvaro.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesse.edvaro.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM uesr_table")
    fun getUser(): LiveData<List<User>>

    @Query("DELETE FROM uesr_table")
    suspend fun deleteUser()
}