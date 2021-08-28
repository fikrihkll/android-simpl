package com.teamdagger.simpl.data.room.desc

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "desc")
data class DescCacheEntity(
    @PrimaryKey
    @ColumnInfo(name="desc_name")
    var descName:String
)
