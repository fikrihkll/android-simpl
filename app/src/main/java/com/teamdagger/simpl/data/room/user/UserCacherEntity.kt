package com.teamdagger.simpl.data.room.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserCacherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Int,
    @ColumnInfo(name="name")
    var name: String,
    @ColumnInfo(name="pict")
    var pict: String?,
    @ColumnInfo(name="email")
    var email: String
)