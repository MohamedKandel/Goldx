package com.correct.goldx.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UsersDB : RoomDatabase() {
    abstract fun dao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: UsersDB? = null
        fun getDBInstance(context: Context): UsersDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsersDB::class.java,
                    "usersDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}