package com.example.fitnesstracker.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Calc::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase(){

    abstract fun calcDao(): CalcDao

    companion object{

        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {

            return if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "fitness_tracker"
                    ).build()
                }
                INSTANCE as AppDataBase
            } else {
                INSTANCE as AppDataBase

            }
        }
    }
}