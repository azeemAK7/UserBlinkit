package com.example.userblinkit.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProduct::class], version = 1, exportSchema = false)
abstract class CartProductDB : RoomDatabase() {

    abstract fun cartProductDAO() : CartProductDao

    companion object{

        @Volatile
        var INSTANCE : CartProductDB ? = null
        fun getDatabaseInstance(context : Context) : CartProductDB{
            val tempInstance = INSTANCE
            if(tempInstance != null) return  tempInstance
            synchronized(this){
                val roomdb = Room.databaseBuilder(context,CartProductDB::class.java,"CartProduct").allowMainThreadQueries().build()
                INSTANCE = roomdb
                return roomdb
            }
        }

    }

}