package com.jesse.edvaro.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesse.edvaro.data.model.Ride

@Dao
interface RideDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRides(rides: List<Ride>)

    @Query("SELECT * FROM ride_table")
    fun getAllRides(): LiveData<List<Ride>>

    @Query("DELETE FROM ride_table")
    suspend fun deleteRides()
}