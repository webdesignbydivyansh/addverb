package com.example.addverb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CountryDao {
    @Insert
    suspend fun insertCountry(countryModel: CountryModel)

    @Query("Select * from CountryModel")
     fun getTask(): List<CountryModel>

    @Query("Delete from CountryModel")
    suspend fun deleteAll()
}