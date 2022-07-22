package com.jesse.edvaro.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jesse.edvaro.data.database.dao.RideDao
import com.jesse.edvaro.data.database.dao.UserDao
import com.jesse.edvaro.data.database.typeconverter.ListConverter
import com.jesse.edvaro.data.model.Ride
import com.jesse.edvaro.data.model.User

@Database(entities = [Ride::class, User::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class EdvoraDatabase: RoomDatabase() {

    abstract fun rideDao(): RideDao

    abstract fun userDao(): UserDao
}