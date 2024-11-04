package com.correct.goldx.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("select * from user")
    suspend fun getAll(): MutableList<User>?

    @Query("select * from user where id= :id")
    suspend fun getOneUser(id: String): User?

    @Query("delete from user where id= :id")
    suspend fun deleteUser(id: String)
}