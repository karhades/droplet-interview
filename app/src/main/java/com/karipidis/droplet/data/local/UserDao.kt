package com.karipidis.droplet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("select * from users where id = :userId")
    suspend fun getUser(userId: String): LocalUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(localUser: LocalUser)

    @Query("delete from users")
    suspend fun deleteUsers()
}