package com.example.addverb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CountryModel::class],version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun countryDao():CountryDao
}