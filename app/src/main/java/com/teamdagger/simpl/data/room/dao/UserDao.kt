package com.teamdagger.simpl.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teamdagger.simpl.data.room.user.UserCacherEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewUser(user:UserCacherEntity):Long

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUser(userId:Int):UserCacherEntity
}