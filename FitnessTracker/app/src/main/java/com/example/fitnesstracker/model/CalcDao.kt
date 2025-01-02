package com.example.fitnesstracker.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CalcDao {

    @Insert
    fun insert(calc: Calc)

    @Query("SELECT * FROM Calc where type = :type")
    fun getRegisterByType(type: String) : List<Calc>

    @Query("SELECT * FROM Calc")
    fun getAllRegister() : List<Calc>

}