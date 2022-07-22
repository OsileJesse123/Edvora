package com.jesse.edvaro.data.database.typeconverter

import androidx.room.TypeConverter

class ListConverter {

    @TypeConverter
    fun convertToString(list: List<Int>): String = list.joinToString("@")

    @TypeConverter
    fun convertToList(string: String): List<Int> = (string.split("@")) as List<Int>
}