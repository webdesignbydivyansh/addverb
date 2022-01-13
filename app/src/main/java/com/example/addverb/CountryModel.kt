package com.example.addverb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryModel (
    val common:String,
    val capital:String,
    val png:String,
    val region:String,
    val subregion:String,
    val population:Int,
    val borders:String,

    @PrimaryKey(autoGenerate = true)
    val id:Long=0
        )